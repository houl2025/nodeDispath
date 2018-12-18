package dispatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

public class dispatcher {

	
   List <computerNode> currentNodeArray ;// 由计算机节点组成的集合
	 
   //计算性能时默认各指标权重
   float []  default_k_Ratio=new float[] {(float) 0.2,(float) 0.3,0,(float) 0.5};
   
   float currentMinLoadW;//计算机节点集合currentNodeArray中的最小负载权值
   float currentMaxLoadW;//计算机节点集合currentNodeArray中的最大负载权值
   float currentMeanLoadW;//计算机节点集合currentNodeArray中的平均负载权值
   
   float currentMinRisk;//计算机节点集合currentNodeArray中的最小风险
   float currentMaxRisk;//计算机节点集合currentNodeArray中的最大风险
   float currentMeanRisk;//计算机节点集合currentNodeArray中的平均风险
  
   //
 
	public boolean needRisk=true;//是否考虑风险
	
	public boolean needLoadStatus=true;//是否需要考虑负载
	
   float riskThreshold;// 风险阈值，用于第一步筛选，将风险较高的节点从候选集合中筛除
   float LoadThreshold;//负载权值阈值， 用于第二步筛选，将负载权值过大的节点从候选集合中筛除
   
   List<float[]>  HeteroAttributeWeight; //异构相关属性的的权值
   
   List<float[]> HeteroAttribute ; // 保存经过处理过后的数据，结合 HeteroAttributeWeight ，用于聚类；其中列表中每一个float[]数组对应一个节点
   
   
   weightKMean KMC;// 用于聚类的实例
   
	public static void main(String[] args) {
		
		System.out.println("main");
		// TODO Auto-generated method stub
		//
		List <computerNode> NodeArray =null;
		NodeArray=getAllNodes();
		//需要的节点数
		int NeedNum=3;
	
		
		
		//初始化一个dispatcher类实例
		dispatcher adispatcher =new dispatcher(NodeArray);
		/*
		HashMap <String , List<float[]>>NodeInfoMap=(HashMap<String, List<float[]>>) adispatcher.vectorTransform();
		//调用实例方法，得到需要的结果
		boolean b=adispatcher.setAttribute(NodeInfoMap);
		System.out.println("b="+b);
		adispatcher.showHeterAttributes();
		
		
		adispatcher.setAttributeValueAndWeight();
	   adispatcher.KMC=new weightKMean(NeedNum);
	   
	    NodeArray=new ArrayList<>();
	    computerNode cn0=adispatcher.currentNodeArray.get(0);
	    computerNode cn1=adispatcher.currentNodeArray.get(1);
	    NodeArray.add(cn0.copyValue("01"));
	    NodeArray.add(cn0.copyValue("02"));
	    NodeArray.add(cn0.copyValue("03"));
	    NodeArray.add(cn0.copyValue("04"));
	    
	    NodeArray.add(cn1.copyValue("11"));
	    NodeArray.add(cn1.copyValue("12"));
	    NodeArray.add(cn1.copyValue("13"));
	    
	   // adispatcher.KMC.setDataSet(NodeArray);
		//adispatcher.KMC.setDataSet(adispatcher.currentNodeArray);
		*/
		//adispatcher.KMC.kmeans();
	    List <String> NodeId=null;
		//List <String> NodeId=adispatcher.KMC.getIDFromClusterCenter();
		
		/*
		for(int s=0;s<NodeId.size();s++){
			System.out.println(NodeId.get(s));
		}
		*/
	    
		NodeId=adispatcher.assignID(NeedNum);
		
		for(int s=0;s<NodeId.size();s++){
			System.out.println(NodeId.get(s));
		}
		//int [] NODEID=adispatcher.getHeteroNodes(NeedNum);
		
		//
		

		

	}
	
	public  dispatcher( List <computerNode> NodeArray){
		currentNodeArray=NodeArray;
	}
	/**
	 *  用新的节点集替换当前节点集
	 * @param NodeArray
	 */
	public void replaceNodeArray(List <computerNode> NodeArray){
		currentNodeArray=NodeArray;
	}
   public  dispatcher(){
		
	}
	
  /**
   * 获得当前系统中所有可用节点
   * @return 当前系统中所有可用节点组成的列表
   */
	public static  List<computerNode> getAllNodes(){
		 List<computerNode>  cN=null;
		 Random random = new Random(100);  
		int temp = random.nextInt(20);  
	   int N=6+temp;
	   int m=4;
	   int T=random.nextInt(20)+10;
	   int hn=2+random.nextInt(5); 
	   int vvn;
	   int tvn;
	   float[] cp ;
	   float [] ld;
	  
	   float[] HA;
	   int [] AT;
	   AT=new int[hn];
	   float t;
	   for(int i=0;i<hn;i++){
		   t=random.nextFloat();
		   if(t<0.5){
			   AT[i]=-1;
		   }else{
			   AT[i]=1;
		   }
	   }
	   
	   cN=new ArrayList<>();
	   for(int i=0;i<N;i++){
		   computerNode cn=new computerNode();
		   tvn=random.nextInt(20);
		   vvn=(int) (tvn*random.nextFloat());
		   cp=new float[m];
		   ld=new float[m];
		   for(int j=0;j<m;j++){
			   cp[j]=T*random.nextFloat();
			   ld[j]=T*random.nextFloat();
		   }
		   
		   HA=new float[hn];
		   for(int j=0;j<hn;j++){
			   t=random.nextFloat();
			   
			   if( AT[j]<0){
				   HA[j]=random.nextInt(5);
			   }else{
				   HA[j]=random.nextInt(10);
			   }
		   
		   }
		   cn.ID=String.valueOf(i);
		   cn.Capability=cp;
		   cn.Load=ld;
		   cn.totalVisitNum=tvn;
		   cn.validAttackNum=vvn;
		   cn.AttributeType=AT;
		   cn.HeteroAttribute=HA;
		   cN.add(cn);
		   cn.showNodeInfo();
		   
	   }
		 
		 
		 return cN;
	}
	
	/**
	 * 
	 * 
	 * @param LeastNum 返回的数组元素个数尽量不小于 LeastNum；
	 * @return 风险值小于风险阈值的节点组成的集合
	 */
	public  List <computerNode> chooseNodeByRisk( int NodeNum){
		List <computerNode> NodeArray=new ArrayList<>();
		int i=0;
		for(int j=0;j<currentNodeArray.size();j++) {
			
			//i<currentNodeArray.size();
			if (currentNodeArray.get(j).risk<0.8){
				
			}
			i++;
		}
		
		if (i<NodeNum) {//;//需要定义节点请求个数
			 NodeArray=currentNodeArray;
		}
		//直接跳过风险评估部分，节点池中全部节点进入负载计算；
		else {
			
			for(int j=0;j<currentNodeArray.size();j++) {
				if (currentNodeArray.get(j).risk<0.9){
					NodeArray.add(currentNodeArray.get(j));
				}
			}
			
		}
				
				//把这个风险小于0.9的节点集合传给下一步负载。
		                                
		
		//List <computerNode> NodeArray=null;
		return NodeArray;
	}
	
	public void getLoadW(float []k){
		   for(int j=0;j<currentNodeArray.size();j++) {
			   computerNode cn=currentNodeArray.get(j);
		      float[] Capability=cn.Capability;
		      float CNode = k[0]*Capability[0]*cn.cpu_Num;
		 
			   float[] Load=cn.Load;
			   float LNode=0;
			   for(int i=1;i<Capability.length;i++)
			      {
				   CNode += Capability[i]*k[i];
				   }
			   
			      for(int i=0;i<Load.length;i++) {
					   LNode+=Load[i]*k[i];
				   }
			      
		      cn.Capa=CNode;
			   cn.Loa=LNode;
			   cn.LoadW=cn.Loa/cn.Capa;  
			   System.out.println(j+"*"+cn.LoadW+":"+ cn.Capa);
		   }
		   float max=-1;
		   for(int j=0;j<currentNodeArray.size();j++) {
			   computerNode cn =currentNodeArray.get(j);
			   if( cn.LoadW>max) {
				   max=cn.LoadW;
			   }
		   }
		   for(int j=0;j<currentNodeArray.size();j++) {
			  computerNode cn =currentNodeArray.get(j);
			  float LoadD=(max-cn.LoadW)*cn.Capa;
			  cn.LoadD=LoadD;
		   }	
		   
		  
		   

		}
		
	
	/**
	 * 
	 * @param wThreshold 负载权重阈值
	 * @param LeastNum 返回的数组元素个数尽量不小于 LeastNum；
	 * @return 风险值小于风险阈值的节点组成的集合
	 */
	public void orderNodeByLoadW( float wThreshold,int LeastNum){
		List <computerNode> NodeArray=null;
		//List<computerNode> currentNodeArray=
		computerNode temp=null;
		int size = currentNodeArray.size();
		for(int i=0;i<size-1;i++)
		{
			for(int j=0;j<size-1-i;j++)
			{
				if(currentNodeArray.get(j).LoadW>currentNodeArray.get(j+1).LoadW){//交换两数位置，负载较小的往前排
					temp = currentNodeArray.get(j);
					currentNodeArray.set(j, currentNodeArray.get(j+1));
					currentNodeArray.set(j+1, temp);
				
				}
					
				}
					
			}
		}
	
	
	 public float getMaxRisk() {
		   
		   
		   for(int i=0;i<this.currentNodeArray.size();i++) {
			  
				computerNode cn=currentNodeArray.get(i);
				cn.risk=cn.validAttackNum/cn.totalVisitNum;
		     
		   }
		   float max=Float.MIN_VALUE;
		   computerNode 	acn;
		   for(int i=0;i<this.currentNodeArray.size();i++) {
			   
		   if (currentNodeArray.get(i).risk>max) {
			   max=currentNodeArray.get(i).risk;
			  
		   }
		  
	   }
		
		return max;
 }
	

	
	
	
	/**
	 * 
	 * @param wThreshold 负载权重阈值
	 * @param LeastNum 返回的数组元素个数尽量不小于 LeastNum；
	 * @return 风险值小于风险阈值的节点组成的集合
	 */
	public  List <computerNode> chooseNodeByLoadW( int LeastNum){
		List <computerNode> NodeArray=null;
		return NodeArray;
	}
	

	
	public List<Float> getNodeArrayHeteroAttribute(int index){
		int m=this.currentNodeArray.size();
		if(index>=m){
			return null;
		}
		List<Float> HA=new ArrayList<>();
		float att;
		
		for(int s=0;s<m;s++){
			computerNode  tmpcn=currentNodeArray.get(index);
			att=tmpcn.HeteroAttribute[index];
			HA.add(att);
		}
		return HA;
	}
	
	/**
	 * 距离公式,用于聚类
	 * @param np1  
	 * 
	 * 
	 * 
	 * @param np2
	 * @return
	 */
	public float distance(float[] np1,float[] np2,List<float[]> w){
		float d=-1;
		return d;
	}
	/**
	 * 
	 * @param s 
	 * @return currentNodeArray中节点的ID组成的数组
	 */
	public List<String >getNodeID(int[] s){
		int len=s.length;
		List<String> cnIDs=new ArrayList<>();
		String v;
		for(int i=0;i<len;i++){
			v=this.currentNodeArray.get(s[i]).ID;
			cnIDs.add(v);
		}
		
		return cnIDs;
	}
	

	
	
	
	
	/**
	 * 根据 currentNodeArray ，将节点异构相关属性的数值向量化
	 * @retur  map={"HeteroAttribute": List<float[]>, "HeteroAttributeWeight":List<float[]>}
	 */
	public Map<String ,List<float[]>>  vectorTransform(){
		if( this.currentNodeArray==null|| this.currentNodeArray.size()<1){
			return null;
		}
		HashMap<String ,List<float[]>> NodeInfoMap = new HashMap<String ,List<float[]>>();
		int currentNodeArraySize=currentNodeArray.size();
		System.out.println("currentNodeArraySize="+currentNodeArraySize);
		computerNode cn=currentNodeArray.get(0);
		
		int attributeDim=cn.HeteroAttribute.length;
		
		int attributeType;
		List<Float>attributeValue;
	
		

		List<float[]> HeteroAttribute=new ArrayList<>();
		List<float[]> HeteroAttributeWeight=new ArrayList<>();
		List<Integer>subDim=new ArrayList<>();
		List<Integer>validAttributeIndex=new ArrayList<>();
		List<Integer> validAttributeType=new ArrayList<>();
		HashMap<Integer ,HashMap<String ,List<float[]>>  > VectorMap = new HashMap<>();
		
		HashMap<String ,List<float[]>> tmpTrans;
		List<Float> AllPart=new ArrayList<>();
		List<float[]> vector;
		int ALength=0;
		float[] w;
		float [] attVector;
		float v;
		int singleD;
		float[] singleAtt;
		boolean validIndex=false;
	
		for(int i=0;i<attributeDim;i++){
			attributeValue=new ArrayList<>();
			attributeType=cn.AttributeType[i];
			tmpTrans=null;
			validIndex=false;
			System.out.println(i+":attributeType= "+attributeType);
			for(int j=0;j<this.currentNodeArray.size();j++){
				v=this.currentNodeArray.get(j).HeteroAttribute[i];
				//System.out.print(":"+j+"->"+v+"   ");
				attributeValue.add(v);
			}
			System.out.println(" ");
			
			if(attributeType==-1){
				validIndex=true;
				tmpTrans=categoricalDataVectoring(attributeValue);
			}else if(attributeType==1){
				validIndex=true;
				tmpTrans=continueDataVectoring(attributeValue);
			}
			
			if(validIndex==true && tmpTrans!=null){
				if(tmpTrans.size()>0){
					 w=tmpTrans.get("part").get(0);
					 singleD=w.length;
					 //System.out.println(" part ");
					 for(int k=0;k<singleD;k++){
						 AllPart.add(w[k]);
						// System.out.print(w[k]+" ");
					 }
					 //System.out.println(" ");
					 ALength=ALength+singleD;
					 subDim.add(singleD);
					 VectorMap.put(i, tmpTrans);
					 validAttributeIndex.add(i);
					 validAttributeType.add(attributeType);
				}
			}
			
		}
		System.out.println("ALength="+ALength);
		
		if(ALength<1){
			return null;
		}
		for(int j=0;j<this.currentNodeArray.size();j++){
			singleAtt=new float[ALength];
			HeteroAttribute.add(singleAtt);
		}
		
		int start=0;
		int Len=0;
		int s;
		for(int i=0;i<validAttributeIndex.size();i++){
			Len=subDim.get(i);
			s=validAttributeIndex.get(i);
			tmpTrans=VectorMap.get(s);
			vector=tmpTrans.get("vector");
			
			//System.out.println("i="+i+"：s="+s+":start="+start+":Len="+Len+":"+"vector.size()="+vector.size());
			//System.out.println("HeteroAttribute.size()="+HeteroAttribute.size());
				for(int j=0;j<HeteroAttribute.size();j++){
					attVector=vector.get(j);
					singleAtt=HeteroAttribute.get(j);
					for(int k=0;k<Len;k++){
						singleAtt[start+k]=attVector[k];
					}
				}
				start=start+Len;
		};
		
		//weight
		HeteroAttributeWeight=this.generateWeight(validAttributeType, subDim, AllPart);
		
		
		NodeInfoMap.put("HeteroAttribute", HeteroAttribute);
		NodeInfoMap.put("HeteroAttributeWeight", HeteroAttributeWeight);
		
		return NodeInfoMap;
				
	}
	
	public List<float[]> generateWeight(List<Integer> validAttributeType,List<Integer>subDim,List<Float> AllPart){
		List<float[]> Result=new ArrayList<>();
		int AttLen=subDim.size();
		int allPartLen=AllPart.size();
		float[] firstWeight=new float[AttLen];
		float[] secondWeight=new float[allPartLen];
	
		float sum=0;
		int N;
		int type;
		int iStart=0;
		double S;
		float P=AttLen;
		for(int i1=0;i1<AttLen;i1++){
			N=subDim.get(i1);
			sum=0;
			float minf=Float.MAX_VALUE;
			float maxf=-Float.MAX_VALUE;
			float a=0;
			for(int i2=0;i2<N;i2++){
				a=AllPart.get(i2+iStart);
				sum=sum+a;
				if(a>maxf){
					maxf=a;
					
				}
				
				if(a<minf){
					minf=a;
				}
			}
			iStart= (N+iStart);
			if(N>1){
				S=N;
				S=S*Math.log(S);
				sum=(float) (sum/S);
			}
			if(sum==0){
				sum=1;
			}
			firstWeight[i1]=1/(sum*P);
			
		}
		
		firstWeight=normalEqualTo1(firstWeight);
		
		iStart=0;
		for(int i1=0;i1<AttLen;i1++){
			N=subDim.get(i1);
			float[] tmp=new float[N];
			for(int i2=0;i2<N;i2++){
				tmp[i2]=( 1/AllPart.get(i2+iStart) );
			}
			tmp=normalEqualTo1(tmp);
			for(int i2=0;i2<N;i2++){
				secondWeight[i2+iStart]=tmp[i2]*firstWeight[i1];
				
			}
			
			iStart= (N+iStart);
			
		}
		
		Result.add(secondWeight);
		return Result;
		
	}
	
	public float[] normalEqualTo1(float[] data){
		int len=data.length;
		float [] ndata=new float[len];
		float count=0;
		for(int k=0;k<len;k++){
			count=data[k]+count;
		}
		if(count!=0){
			for(int k=0;k<len;k++){
				ndata[k]=data[k]/count;
			}
		}
		return ndata;
	}
	
	public HashMap<String ,List<float[]>> categoricalDataVectoring(List<Float> data){
		if(data==null || data.size()<1){
			return null;
		}
		HashMap<String ,List<float[]>> dataMap=null;
		List<float[]> dataVector=new ArrayList<>();
		HashMap <Float,Integer>TypeKinds=new HashMap<Float, Integer>();
		int kind=0;
		float key;
		int value=0;
		Integer tmp;
		int totalN=0;
		for(int i=0;i<data.size();i++){
			key=data.get(i);
			tmp=TypeKinds.get(key);
			if(tmp==null){
				kind=1;
				TypeKinds.put(key, kind);
			}else{
				value=tmp;
				kind=value+1;
				TypeKinds.put(key, kind);
			}
			totalN++;
			
		}
		 
		int Type=TypeKinds.size();
		if(Type==1){
			return null;
		}else{
			
			float[] TypeKey=new float[Type];
			int j=0;
			//
			for (Entry<Float, Integer> entry : TypeKinds.entrySet()) {
				//System.out.println(entry.getKey());
				TypeKey[j]=entry.getKey();
				j++;
			}
			
			
			//排序
			List<Float>orderKey=new ArrayList<>();
			
			boolean hasFind=false;
			float currentv;
			int s=0;
			float kv;
			for(j=0;j<Type;j++){
				currentv=TypeKey[j];
				s=orderKey.size()-1;
				hasFind=false;
				for( ;s>=0;s--){
					kv=orderKey.get(s);
					if(currentv>=kv){
						hasFind=true;
						break;
					}
				}
				if(hasFind){
					orderKey.add(s+1,currentv );
				}else{
					orderKey.add(0,currentv );
				}
				
				
			}
			
			List<float[]>partH=new ArrayList<>();
			float[] H=new float[Type];
			int num;
			float ph=0;
			double tmpValue;
			double HXY=0;
			for(j=0;j<Type;j++){
			
				num=TypeKinds.get(orderKey.get(j));
				tmpValue= ((1.0*num)/totalN);
				
				tmpValue=-tmpValue*(Math.log(tmpValue));
				ph=(float) tmpValue;
				H[j]=ph;
				HXY=HXY+ph;
				System.out.println(orderKey.get(j)+"->"+num+"/"+totalN+"="+tmpValue+":pH="+ph);
			}
			partH.add(H);
			
			for(int i=0;i< data.size();i++){
				float []vector=new float[Type];
				hasFind=false;
				currentv=data.get(i);
				for(j=0;j<Type;j++){
					
					kv=orderKey.get(j);
					
					if(currentv==kv){
						hasFind=true;
						break;
					}
					
				}
				//System.out.println(currentv+":hasfind:"+j);
				vector[j]=1;
				dataVector.add(vector);
				
			}
			dataMap= new HashMap<String, List<float[]>>();
			String Key1="vector";
			
			String Key2="part";
			double NN=dataVector.get(0).length;
			NN=NN*Math.log(NN);
			double E=HXY/NN;
			System.out.println("cata:"+dataVector.size()+": "+dataVector.get(0).length+":HXY="+HXY+":E="+E);
			dataMap.put(Key1, dataVector);
			dataMap.put(Key2, partH);
			
		}
	  
		return dataMap;
		
		
	}
	
	
	
	public HashMap<String ,List<float[]>> continueDataVectoring(List<Float> data){
		HashMap<String ,List<float[]>> dataMap=null;
		if(data==null || data.size()<1){
			return null;
		}
		float Sum=0;
		float Max=-Float.MAX_VALUE;
		float Min=Float.MAX_VALUE;
		float v;
		int totalN=0;
		for(int i=0;i<data.size();i++){
			v=data.get(i);
			Sum=Sum+v;
			if(Max<v){
				Max=v;
			}
			if(Min>v){
				Min=v;
			}
			totalN++;
		}
		
		if(Min==Max){
			return null;
		}
		float [] normalData=new float[totalN];
	
		for(int i=0;i<totalN;i++){
			v=data.get(i);
			//System.out.print(v+"->");
			v=(v-Min)/(Max-Min);
          // System.out.println(v);
			normalData[i]=v;
		}
		double H=0;;
		
		normalData=normalEqualTo1(normalData);
		
		for(int i=0;i<totalN;i++){
			v=normalData[i];
			//System.out.print(v+"   ");
			if(v>0){
				H=H-v*Math.log(v);
			}
			
		}
		//System.out.println(" ");
		H=H/(Math.log(totalN));
		float[] E=new float[1];
		E[0]=(float) H;
		System.out.println("continue:totalN:"+totalN+"  :H "+H);
		float [] vec;
		List<float[]> dataVector=new ArrayList<>();
		List <float[]> part=new ArrayList<>();
		
		dataMap=new HashMap<String ,List<float[]>>();
		part.add(E);
		
		
		for(int i=0;i<totalN;i++){
			vec=new float[1];
			vec[0]=normalData[i];
			dataVector.add(vec);
		}
		
		String Key1="vector";
		
		String Key2="part";
		//System.out.println("vector:"+dataVector.size());
		dataMap.put(Key1, dataVector);
		dataMap.put(Key2, part);
		
		return dataMap;
	}
	

	public void showHeterAttributes(){
    	System.out.println("showHeterAttributes");
    	if(this.HeteroAttributeWeight==null){
    		System.out.println("HeteroAttributeWeight==null");
    	}else{
    		System.out.println("HeteroAttributeWeight:");
        	for(int i=0;i<this.HeteroAttributeWeight.size();i++){
        		float[] vv=this.HeteroAttributeWeight.get(0);
        		System.out.println("index:"+i);
        		for(int j=0;j<vv.length;j++){
        			System.out.print(vv[j]+"   ");
        		}
        		System.out.println("");
        	}
    		
    	}
    	if(this.HeteroAttribute==null){
    		System.out.println("HeteroAttribute==null");
    	}else{
    		System.out.println("HeteroAttribute:");
        	for(int i=0;i<this.HeteroAttribute.size();i++){
        		float[] vv=this.HeteroAttribute.get(i);
        		System.out.println("index:"+i);
        		for(int j=0;j<vv.length;j++){
        			System.out.print(vv[j]+"   ");
        		}
        		System.out.println("");
        	}
    	}
    
    }
	
	  public boolean setAttribute(Map<String ,List<float[]>>NodeInfoMap){
		  boolean b=false;
		  if(NodeInfoMap!=null){
			  if(NodeInfoMap.size()>1){
					this.HeteroAttribute=NodeInfoMap.get("HeteroAttribute");
					this.HeteroAttributeWeight=NodeInfoMap.get("HeteroAttributeWeight");
					if(this.HeteroAttribute!=null && this.HeteroAttributeWeight!=null){
						b= true;
					}
			  }
		  }
		  
		  return b;
	  }
	  
	  public void setAttributeValueAndWeight(){
		 for(int i=0;i<this.currentNodeArray.size();i++){
			 this.currentNodeArray.get(i).AttributeVector=this.HeteroAttribute.get(i);
			 this.currentNodeArray.get(i).AttributeWeight=this.HeteroAttributeWeight.get(0);
			 
		 }
	  }
	  
	  public List<String> getAllId(List<computerNode> cnArray){
			if(cnArray==null){
				return null;
			}else{
				List<String> rid=new ArrayList<>();
				for(int i=0;i<cnArray.size();i++){
					rid.add(cnArray.get(i).ID);
				}
				return rid;
			}
		}
	  
	  public List<String> assignID(int NeedNum){
	    	List<String> ids=null;
	    	if(this.currentNodeArray==null){
	    		return null;
	    	}else if(this.currentNodeArray.size()<NeedNum){
	    		return getAllId(this.currentNodeArray);
	    	}else{
	    		ids=new ArrayList<>();
	    		
	    	  if(this.needRisk){
	    		  
	    	  }
	    	  if(this.needLoadStatus){
	    		  
	    	  }
	    		HashMap <String , List<float[]>>NodeInfoMap=(HashMap<String, List<float[]>>) vectorTransform();
	    		//调用实例方法，得到需要的结果
	    		boolean b=setAttribute(NodeInfoMap);
	    		System.out.println("setAttribute->b="+b);
	    		if(b){
	    			showHeterAttributes();
	    			//
	    			
	    			setAttributeValueAndWeight();
	    			KMC=new weightKMean(NeedNum);
	    			KMC.setDataSet(currentNodeArray);
	    			KMC.kmeans();
	    			ids=KMC.getIDFromClusterCenter();
	    			for(int s=0;s<ids.size();s++){
	        			System.out.println(ids.get(s));
	        		}
	    			
	    			
	    		}else{
	    			ids=this.randomAssignID(NeedNum);
	    		}
	    		
	    			
	    	}
	    	
	    	return ids;
	 	 
	    }
	  
	  
	  public List<String> randomAssignID(int NeedNum){
	    	
	    	
	    	List<String> ids=null;
	    	if(this.currentNodeArray==null){
	    		return null;
	    	}else if(this.currentNodeArray.size()<NeedNum){
	    		return getAllId(this.currentNodeArray);
	    	}
	    		int dataSetLength=this.currentNodeArray.size();
	        int k= NeedNum;
	        int[] randoms = new int[k];  
	        boolean flag;  
	        Random random=new Random();
	        int temp = random.nextInt(dataSetLength);  
	        randoms[0] = temp;  
	        for (int i = 1; i < k; i++) {  
	            flag = true;  
	            while (flag) {  
	                temp = random.nextInt(dataSetLength);  
	                int j = 0;  
	                while (j < i) {  
	                    if (temp == randoms[j]) {  
	                        break;  
	                    }  
	                    j++;  
	                }  
	                if (j == i) {  
	                    flag = false;  
	                }  
	            }  
	            randoms[i] = temp;  
	        }   
	        ids=new ArrayList<>();
	        for (int i = 0; i < k; i++) {  
	        	String s=this.currentNodeArray.get(randoms[i]).ID;
	        	
	        	ids.add(s);
	              
	        }  
	        return ids;  
	    }
	    
	    
	
}

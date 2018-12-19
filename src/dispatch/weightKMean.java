package dispatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class weightKMean {

	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	
	    public int k;// 分成多少簇  
	    public int m=2000;// 迭代次数  
	    public int print_Gap=100;//每迭代100次打印一次信息；
	    public int dataSetLength;// 数据集元素个数，即数据集的长度  
	    public float mindistance;// 数据集元素间最小非零距离，如果数据集元素都相同，则mindistance
	    
	    public ArrayList<ArrayList<computerNode>> primeCluster; //簇中每一个集合中的各元素距离相等；
	    public List<computerNode> dataSet;// 数据集链表  
	    public ArrayList<computerNode> center;// 中心链表  
	    public ArrayList<ArrayList<computerNode>> cluster; // 簇  
	    public ArrayList<Float> jc;// 误差平方和，k越接近dataSetLength，误差越小  
	    private Random random;  
	   
	    public void setDataSet(List<computerNode>  dataSet) {  
	    	//设置需分组的原始数据集
	        this.dataSet = dataSet;  
	    }  
	 
	    public ArrayList<ArrayList<computerNode>> getCluster() {  
	        return cluster;  
	    }  
	   
	    public weightKMean(int k) {  
	    	//传入需要分成的簇数量
	        if (k <= 0) {  
	            k = 1;  
	        }  
	        this.k = k;  
	    }  
	    
	    public void setK(int k) {  
	    	//传入需要分成的簇数量
	       
	        this.k = k;  
	    }  
	  
	  public ArrayList<computerNode> getNearstData(ArrayList<computerNode> A,computerNode cn){
		  ArrayList<computerNode>  NearstCN=new ArrayList<>();
		  if(A==null || A.size()<1){
			  return null;
		  }
		  float mindis=Float.MAX_VALUE;
		  float d;
		  for(int i=0;i<A.size();i++){
			  d=distance(cn, A.get(i));
			  if(d<mindis){
				  mindis=d;
			  }
		  }
		  for(int i=0;i<A.size();i++){
			  d=distance(cn, A.get(i));
			  if(d==mindis){
				  NearstCN.add(A.get(i));
			  }
		  }
		  
		  return  NearstCN;
		  
	  }
	  
	  public computerNode  getMeanData(ArrayList<computerNode> A){
		  computerNode meancn=new computerNode();
		  if(A==null || A.size()<1){
			  return null;
		  }
		  
		  
		   int DLen=A.get(0).AttributeVector.length;
		   meancn.AttributeVector=new float[DLen];  
     	   meancn.AttributeWeight=A.get(0).AttributeWeight;
         for (int j = 0; j < A.size(); j++) {  
         	  for(int s=0;s<DLen;s++){
         		  meancn.AttributeVector[s]=+A.get(j).AttributeVector[s];
         	      }
            
         	}  
         for(int s=0;s<DLen;s++){
    		  meancn.AttributeVector[s]= meancn.AttributeVector[s]/A.size();
    	      }
		  
		  return meancn;
		 
	  }
	  
	    private void init() { 
	    	//初始化
	        //m = 0;  
	        random = new Random();  
	        if (dataSet == null || dataSet.size() == 0) {   
	        	System.out.println("数据为空，请输入数据！！！！");
	        } else{
	        	dataSetLength = dataSet.size();  
	        	if (k > dataSetLength) {  
	        		k = dataSetLength;  
	        	}  
	        	System.out.println("k="+k);
	         initPrimeCluster();
	         System.out.println("primeCluster.size="+this.primeCluster.size());
	         System.out.println("initCenterFromPrimeCluster>>>>");
	        	center = initCenterFromPrimeCluster();  
	        	//center = initCenters();
	        	System.out.println("initCluster>>>>");
	        	cluster = initCluster();  
	        	jc = new ArrayList<Float>();  
	        	//System.out.println("center.size()="+center.size());
	        	//System.out.println("cluster.size()="+cluster.size());
	        	}
	    }  
	  
	    private  ArrayList<computerNode>  initCenters() {  
	    	//初始化中心数据链表，分成多少簇就有多少个中心点
	        ArrayList<computerNode> center = new  ArrayList<computerNode> ();  
	        int[] randoms = new int[k];  
	        boolean flag;  
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
	        for (int i = 0; i < k; i++) {  
	            center.add(dataSet.get(randoms[i]));// 生成初始化中心链表  
	        }  
	        return center;  
	    }  
	  
	 
	    private ArrayList<ArrayList<computerNode>> initCluster() {  
	    	//初始化簇集合
	        ArrayList<ArrayList<computerNode>> cluster = new ArrayList<ArrayList<computerNode>>();  
	        for (int i = 0; i < k; i++) {  
	            cluster.add(new ArrayList<computerNode>());  
	        }  
	  
	        return cluster;  
	    }  
	  
	 
	    private float distance(computerNode cn1, computerNode cn2) {  
	    	
	       float distance = (float) computerNode.distance(cn1, cn2);  
	  
	        return distance;  
	    }  
	  
	  
	    private int minDistance(float[] distance) {  
	    	 //获取距离集合中最小距离的位置
	        float minDistance = distance[0];  
	        int minLocation = 0;  
	       
	        for (int i = 1; i < distance.length; i++) {  
	            if (distance[i] < minDistance) {  
	                minDistance = distance[i];  
	                minLocation = i;  
	            } else if (distance[i] == minDistance) // 如果相等，随机返回一个位置  
	            {  
	                if (random.nextInt(10) < 5) {  
	                    minLocation = i;  
	                }  
	            }  
	        }  
	  
	        return minLocation;  
	    }  
	    
	  private void initPrimeCluster(){
		  primeCluster=new ArrayList<>();
		  float d=Float.MAX_VALUE;
		  float currentDis;
		  boolean hasFind=false;
		  computerNode currentcn;
		  ArrayList<computerNode> Array;
		  for(int i=0;i<dataSet.size();i++){
			   currentcn=dataSet.get(i);
			   hasFind=false;
			   for(int j=0;j<primeCluster.size();j++){
				   if(hasFind){
					   break;
				   }
				   Array=primeCluster.get(j);
				   for(int s=0;s<Array.size();s++){
					   currentDis=distance(currentcn, Array.get(s));
					   if(currentDis==0){
						   hasFind=true;
						   Array.add(currentcn);
						   currentcn.chooseflag=0;
						   currentcn.primeClusterIndex=Array.get(s).primeClusterIndex;
						   break;
					   }else{
						   if(currentDis<d){
							   d=currentDis;
						   }
					   }
				   }
			   }
			   if(!hasFind){
				   Array=new ArrayList<>();
				   Array.add(currentcn);
				   currentcn.chooseflag=0;
				   currentcn.primeClusterIndex=primeCluster.size();
				   primeCluster.add(Array);
			   }
			   
		  }
		  
		  this.mindistance=d;
		  System.out.println("mindistance="+mindistance);
	  }
	 
	    private void clusterSet() {  
	    	//将当前元素放到最小距离中心相关的簇中
	        float[] distance = new float[k];  
	       // System.out.println("dataSetLength="+dataSetLength);
	        for (int i = 0; i < dataSetLength; i++) {  
	            for (int j = 0; j < k; j++) {  
	                distance[j] = distance(dataSet.get(i), center.get(j));  
	                }  
	            int minLocation = minDistance(distance);  
	            cluster.get(minLocation).add(dataSet.get(i));  
	  
	        }  
	    }  
	  
	 
	    private float errorSquare(computerNode cn1, computerNode cn2) {
	    	//求两点误差平方的方法 
	        float d=distance(cn1,cn2);  
	        float errSquare = d*d;
	  
	        return errSquare;  
	    }  
	  
	  
	    private void countRule() {  
	    	//计算误差平方和准则函数方法
	        float jcF = 0;  
	        for (int i = 0; i < cluster.size(); i++) {  
	            for (int j = 0; j < cluster.get(i).size(); j++) {  
	                jcF += errorSquare(cluster.get(i).get(j), center.get(i));  
	  
	            }  
	        }  
	        jc.add(jcF);  
	    }  
	    private void setNewCenter() { 
	    	//设置新的簇中心方法
	    	 
	    	  computerNode cn;
	        for (int i = 0; i < k; i++) {  
	            int n = cluster.get(i).size();  
	            computerNode centercn=new computerNode();
	            
	            centercn=this.getMeanData(cluster.get(i));
	            
	            //ArrayList<computerNode> A=getNearstData(cluster.get(i), centercn);
	               
	            if (n != 0 ) {  
	            	   /*
	            	 cn=cluster.get(i).get(0);
	            	 int DLen=cn.AttributeVector.length;
	            	 centercn.AttributeVector=new float[DLen];  
	            	 centercn.AttributeWeight=cn.AttributeWeight;
	                for (int j = 0; j < n; j++) {  
	                	  for(int s=0;s<DLen;s++){
	                		  centercn.AttributeVector[s]=+cluster.get(i).get(j).AttributeVector[s];
	                	      }
	                   
	                	}  
	                // 设置一个平均值  
	                for(int s=0;s<DLen;s++){
              		  	centercn.AttributeVector[s]=centercn.AttributeVector[s]/n;
              	          }
	                
	               float Mindistance=Float.MAX_VALUE;
	               float dist=0;
	               int findIndex=0;
	               for (int j = 0; j < n; j++) {  
	                	   dist=distance(centercn, cluster.get(i).get(j));
	                	   if(dist<Mindistance){
	                		   Mindistance=dist;
	                		   findIndex=j;
	                	   		}
	                   
	                	} 
	               centercn=cluster.get(i).get(findIndex);
	                  */
	                center.set(i, centercn);  
	            }  
	        }  
	    }  
	    
	    
	   
	    public void printDataArray(ArrayList<computerNode> dataArray,  
	            String info) { 
	    	//打印数据
	    	System.out.println("===================================");
	    	System.out.println(info);
	        for (int i = 0; i < dataArray.size(); i++) {  
	            System.out.println("print:(" +i+ "****"+dataArray.get(i).ID + ")");  
	        }  
	        System.out.println("===================================");  
	    }  
	   public void kmeans() {  
	        init();  
	        // 循环分组，直到误差不变为止  
	        int i=0;
	        System.out.println("m="+m);
	        while (i<=m) { 
	        	 //System.out.println("i="+i);
	        	   if(i%print_Gap==0 ){
	        		   float preSquareEror=0;
	        		   String info="";
	        		    if(i>0){
	        		    	preSquareEror=jc.get(i-1);
	        		    	info=String.valueOf(i)+":center:"+"preSquareEror="+preSquareEror;
	        		    }else{
	        		    	info=String.valueOf(i)+":center:";
	        		    }
	        		   
	        		   printDataArray(this.center,info);
	        	   }
	        	   //System.out.println(" clusterSet()");
	            clusterSet(); 
	           // System.out.println(" countRule() ");
	            countRule();   
	            // 误差不变了，分组完成  
	            if (i!=0) {  
	            	System.out.println("ErorChange:"+1000*(jc.get(i) - jc.get(i - 1)));
	                if (jc.get(i) - jc.get(i - 1) == 0) {  
	                	
	                    break;  
	                     }  
	               
	                 }  
	           // System.out.println("    setNewCenter()  ");
	            setNewCenter();   
	            i++;  
	            cluster.clear(); 
	            //System.out.println("    initCluster()   ");
	            cluster = initCluster();  
	        }  
	        
	        clusterSet(); 
	        System.out.println("itetator："+i);
	    } 
	   
	   private void initChooseflag(){
			   for(int i=0;i<dataSet.size();i++){
				   dataSet.get(i).chooseflag=0;
			   }
	   }
	   /**
	    * 
	    * @return 由聚类中心的ID组成的链表
	    */
	   
	   public List<String> getIDFromClusterCenter(){
		   List<String> R=new ArrayList<String>();
		   if(this.center==null || this.center.size()<1){
			   return null;
		   }
		       
		     // clusterSet();
			   for(int i=0;i<center.size();i++){
				   ArrayList<computerNode> A;
				  
				   A=getNearstData(cluster.get(i),center.get(i));;
				   
				 
				   computerNode cn =A.get(0); 
				   R.add(cn.ID);
			   }
		   
	   
	   
		   
		   
		    return R;
	   }
	   
	   public ArrayList<computerNode> initCenterFromPrimeCluster(){
		   
		 //初始化中心数据链表，分成多少簇就有多少个中心点
	        ArrayList<computerNode> center = new  ArrayList<computerNode> ();  
	        int[] randoms = new int[k]; 
	        int primeDataSetLength=this.primeCluster.size();
	        int [] elementNum=new int[primeDataSetLength];
	        int [] nonChooseNUM=new int[primeDataSetLength];
	        List<int[]> primeIndex=new ArrayList<>();
	        ArrayList<computerNode> dataA=new  ArrayList<computerNode> ();  ;
	        ArrayList<computerNode> RandomCN=new  ArrayList<computerNode> ();  
	        computerNode cn;
	        boolean flag;  
	        int temp = random.nextInt(primeDataSetLength);  
	        for(int q=0;q<primeCluster.size();q++){
	        	 dataA=primeCluster.get(q);
	        	 elementNum[q]=dataA.size();
	        	 nonChooseNUM[q]=dataA.size();
	        	 temp = random.nextInt(elementNum[q]);
	        	 cn=dataA.get(temp);
	        	 cn.chooseflag=1;
	        	 RandomCN.add(cn);
	        	 nonChooseNUM[q]=nonChooseNUM[q]-1;
	          }
	        
	        if(primeDataSetLength<k){
	        	int addN=k-primeDataSetLength;
	        	while(addN>0){
	        		temp=random.nextInt(primeDataSetLength);  
		        	if(nonChooseNUM[temp]>0){
		        		temp = random.nextInt(elementNum[temp]);
		        		cn=dataA.get(temp);
		        		if(cn.chooseflag!=1){
		        			cn.chooseflag=1;
		        			 RandomCN.add(cn);
		    	        	 nonChooseNUM[temp]=nonChooseNUM[temp]-1;
		    	        	 addN=addN-1;
		        		}
		        	}
	        	}
	        	
	        	 center=RandomCN;
	        }else if(primeDataSetLength>k){
	        	 temp = random.nextInt(primeDataSetLength);  
	          	randoms[0] = temp;  
		        for (int i = 1; i < k; i++) {  
		            flag = true;  
		            while (flag) {  
		                temp = random.nextInt(primeDataSetLength);  
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
		        for (int i = 0; i < k; i++) {  
		            center.add(RandomCN.get(randoms[i]));// 生成初始化中心链表  
		        }  
	        	
	        	  
	         }else{
	        	  center=RandomCN;
	          }
	        
	        return center;  
	   }

}


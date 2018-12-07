package dispatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
 
		   
   float riskThreshold;// 风险阈值，用于第一步筛选，将风险较高的节点从候选集合中筛除
   
   float LoadThreshold;//负载权值阈值， 用于第二步筛选，将负载权值过大的节点从候选集合中筛除
   
   List<float[]>  HeteroAttributeWeight; //异构相关属性的的权值
   
   List<float[]> HeteroAttribute ; // 保存经过处理过后的数据，结合 HeteroAttributeWeight ，用于聚类；其中列表中每一个float[]数组对应一个节点
   
   
   weightKMeans KMC;// 用于聚类的实例
   
	public static void main(String[] args) {
		
		System.out.println("main");
		// TODO Auto-generated method stub
		//
		List <computerNode> NodeArray =null;
		NodeArray=getAllNodes();
		//需要的节点数
		int NeedNum=3;
		
		//初始化一个dispatcher类实例
		//dispatcher adispatcher =new dispatcher(NodeArray);
		
		//调用实例方法，得到需要的结果
		
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
	 * 
	 * @param NeedNodeNum  需要的节点数目
	 * @return  节点ID 组成的数组
	 */
  public int[] getHeteroNodes(int NeedNodeNum){
	  int [] NodeIds=new int[NeedNodeNum] ;
	  
	  // 第一步 计算节点集合的风险阈值,
	  getRiskThreshold();
	  
	  //第二步参考风险阈值 和参数 NeedNodeNum；留下合适的节点
	   
	  float rThreshold=this.riskThreshold;
	  List<computerNode>NodeArray =chooseNodeByRisk( rThreshold,NeedNodeNum);
	  
	  //替换原来的节点集
	  this.replaceNodeArray(NodeArray);
	  
	// 第三步 计算节点集合的负载权重,
	  getLoadW();
	  
	// 第四步 计算节点集合的负载权重阈值,
	 getLoadWThreshold();
	 
	//第五步参考负载权重阈值 和参数 NeedNodeNum；留下合适的节点
	 
	 float wThreshold=this.LoadThreshold;
	 NodeArray=chooseNodeByLoadW( wThreshold,NeedNodeNum);
	 
	//替换原来的节点集
	  this.replaceNodeArray(NodeArray);
	  
	  //第六步 ，将节点异构相关属性的数值向量化
	  Map<String ,List<float[]>>VMap= trsanform();
	  
	  this.HeteroAttributeWeight=VMap.get("HeteroAttributeWeight");
	  this.HeteroAttribute=VMap.get("HeteroAttribute");
	  
	  // 第七步，向量化数据初始KMC,并聚类
	  KMC=new weightKMeans(this.HeteroAttributeWeight,this.HeteroAttribute);
	  
	  KMC.setK(NeedNodeNum);
	  KMC.kmeans();
	  
	  //第八步，返回每一簇聚类中最接近聚类中心的索引
	  int[] s=KMC.getIndexFromClusterCenter();
	  
	//第九步，由索引s得到获得currentNodeArray中节点ID数组
	  NodeIds=this.getNodeID(s);
	  
	  
	 // 返回 
	  
	  return NodeIds;
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
		   cn.NodeID=i;
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
	 * 计算 currentNodeArray 的风险阈值，由于第一步筛选
	 * 
	 */
	public void getRiskThreshold(){
		
	}
	
	/**
	 * 计算 currentNodeArray 中的各节点的负载权值，统计得到 currentMinLoadW， currentMaxLoadW，currentMeanLoadW
	 * 
	 */
	
	public void getLoadW(){
		
	}
	
	/**
	 * 计算 currentNodeArray 的负载权重阈值，由于第二步筛选
	 * 
	 */
	public void getLoadWThreshold(){
		
	}
	/**
	 * 
	 * @param rThreshold  风险阈值,
	 * @param LeastNum 返回的数组元素个数尽量不小于 LeastNum；
	 * @return 风险值小于风险阈值的节点组成的集合
	 */
	public  List <computerNode> chooseNodeByRisk( float rThreshold,int LeastNum){
		List <computerNode> NodeArray=null;
		return NodeArray;
	}
	
	/**
	 * 
	 * @param wThreshold 负载权重阈值
	 * @param LeastNum 返回的数组元素个数尽量不小于 LeastNum；
	 * @return 风险值小于风险阈值的节点组成的集合
	 */
	public  List <computerNode> chooseNodeByLoadW( float wThreshold,int LeastNum){
		List <computerNode> NodeArray=null;
		return NodeArray;
	}
	
	/**
	 * 根据 currentNodeArray ，将节点异构相关属性的数值向量化
	 * @retur  map={"HeteroAttribute": List<float[]>, "HeteroAttributeWeight":List<float[]>}
	 */
	public Map<String ,List<float[]>> trsanform(){
		HashMap<String ,List<float[]>> NodeInfoMap = new HashMap<String ,List<float[]>>();
		return NodeInfoMap;
				
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
	public int[] getNodeID(int[] s){
		int len=s.length;
		int[] nodeID=new int[len];
		return nodeID;
	}
}

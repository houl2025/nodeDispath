package dispatch;
/**
 * 
 * @author root
 * 
 *保存每个节点的信息，用于调度
 *
 */ 
public class computerNode {
	public int NodeID; // 计算机节点的主键，  //进行调度前需要知道
	public String IpAdress; //计算机节点的ip ，  //进行调度前需要知道
	
	//负载
   public float[]  Capability; //性能指标的数值 //进行调度前需要知道
   public float[]  Load;// 负载指标的数值  //进行调度前需要知道
    
   public float [] k_Ratio ;//性能指标的权重，由于计算负载权值 //也可以在调度开始时赋值
   
   public float LoadW; // 负载权值
    public float LoadD; // 负载差值
    
   // 节点风险相关的信息
   public float validAttackNum ; //被记录的有效攻击次数  //进行调度前需要知道
   public float  totalVisitNum ; //被记录的总访问次数 //进行调度前需要知道
   
   public float risk;// 节点的相对风险
   // 异构方面有关的信息
   public float[] HeteroAttribute ; //异构相关属性的数值 //调度前需要知道
   
   public int[]  AttributeType;  //异构相关属性的类型，如分类型属性，用 -1 表示，如连续型指标用 1 表示。 //进行调度前需要知道
   
   //
   public int orderValue; //计算机节点的在整个节点集群中的序号， //可能暂时用不到
   
   public  void showNodeInfo(){
	   
	   System.out.println("______________________________");
	   System.out.println("Id:"+""+ NodeID);
	   System.out.println("Capability");
	   show(Capability);
	   System.out.println("Load");
	   show(Load);
	   System.out.println("validAttackNum:"+validAttackNum);
	   System.out.println("totalVisitNum:"+totalVisitNum);
	   System.out.println(" AttributeType");
	   show(AttributeType);
	   System.out.println("HeteroAttribute");
	   show(HeteroAttribute);
	   System.out.println("______________________________");
	   
	   
   }
   public void show(float[] A){
	   if(A!=null){
		   for(int i=0;i<A.length;i++){
			   System.out.print(A[i]+"   ****   ");
		   }
	   }
	
	   
	   System.out.println(" ");
   }
   
   public void show(int [] A){
	   if(A!=null){
		   for(int i=0;i<A.length;i++){
			   System.out.print(A[i]+"   ****   ");
		   }
	   }
	  
	 System.out.println(" ");
   }
   
}

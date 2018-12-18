package dispatch;
/**
 * 
 * @author root
 * 
 *保存每个节点的信息，用于调度
 *
 */ 
public class computerNode {
	public String ID; // 计算机节点的主键，  //进行调度前需要知道
	public String IpAdress; //计算机节点的ip ，  //进行调度前需要知道
	
	//负载
	public int cpu_Num=1;
   public float[]  Capability; //性能指标的数值 //进行调度前需要知道
   public float[]  Load;// 负载指标的数值  //进行调度前需要知道
    
   public float [] k_Ratio ;//性能指标的权重，由于计算负载权值 //也可以在调度开始时赋值
   
   public float LoadW; // 负载权值
    public float LoadD; // 负载差值
    
    public float Capa; // 节点性能Capa
    public float Loa;//节点负载Loa
    public float LoadWMax=0;//负载权值的最大值
    
    
    
   // 节点风险相关的信息
   public float validAttackNum ; //被记录的有效攻击次数  //进行调度前需要知道
   public float  totalVisitNum ; //被记录的总访问次数 //进行调度前需要知道
   
   public float risk;// 节点的相对风险
   // 异构方面有关的信息
   public float[] HeteroAttribute ; //异构相关属性的数值 //调度前需要知道
   
   public int[]  AttributeType;  //异构相关属性的类型，如分类型属性，用 -1 表示，如连续型指标用 1 表示。 //进行调度前需要知道
   
   //
   public int orderValue; //计算机节点的在整个节点集群中的序号， //可能暂时用不到
   
   
   public float[] AttributeVector;
   public float[] AttributeWeight;
   
   public int chooseflag=0; //
   public int primeClusterIndex=0;
   
   
   public  void showNodeInfo(){
	   
	   
	   System.out.println("______________________________");
	   System.out.println("Id:"+""+ ID);
	   System.out.println("cpu_Num:"+cpu_Num);
	   
	   System.out.println("Capability");
	   show(Capability);
	   System.out.println("Load");
	   show(Load);
	   
	   System.out.println("LoadD:"+LoadD);
	   System.out.println("LoadW:"+LoadW);
	   System.out.println("validAttackNum:"+validAttackNum);
	   System.out.println("totalVisitNum:"+totalVisitNum);
	   System.out.println("risk:"+risk);
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
   
   public  computerNode copyValue(String Id){
	   computerNode cn=new  computerNode();
	   cn.ID=Id;
	   cn.IpAdress=this.IpAdress;
	   int len=AttributeVector.length;
	   float []vec=new float[len];
	   float[] ww=new float[len];
	   for(int i=0;i<AttributeVector.length;i++){
		   vec[i]=this.AttributeVector[i];
		   ww[i]=this.AttributeWeight[i];
	   }
	   cn.AttributeVector=vec;
	   cn.AttributeWeight=ww;
	   return cn;
   }
   
   public static float distance(computerNode cn1, computerNode cn2){
	   float d=0;
	   for(int s=0;s<cn1.AttributeVector.length;s++){
		   d=d+cn1.AttributeWeight[s]*cn1.AttributeWeight[s]*(cn1.AttributeVector[s]-cn2.AttributeVector[s])*(cn1.AttributeVector[s]-cn2.AttributeVector[s]);
	   }
	   d=(float) Math.sqrt(d);
	   return d;
   }
   
   
}

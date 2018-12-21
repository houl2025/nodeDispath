package dispatch;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class nodeList {
	//时间单位 s; 容量单位：GB；速率单位：GB/s;
	float [] []  CN_info=new float[][]{
//ID, cpu频率 ，内存总容量，磁盘IO速率 ， 网络吞吐量, CPU占用率，内存占用率，磁盘IO占用率，网络宽带占用率， 异常总次数，判定总次数， cpu类型，cpu指标属性，操作系统类型，操作系统指标属性
		
		
		
	};

	public static void main(String[] args) {
		float[][] R=null;
		// TODO Auto-generated method stub
        try {
		   R=readFile("nodeList.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        nodeList nL=new nodeList();
        nL.CN_info=R;
        List<computerNode> A=transform(nL.CN_info);
        System.out.println(A.size());
           //需要的节点数
      	int NeedNum=3;
      	
      	dispatcher adispatcher =new dispatcher(A);
      	//adispatcher.showHeterAttributes();
      	List<String> NodeId=adispatcher.assignID(NeedNum);
      	//A.get(0).showNodeInfo();
      	adispatcher.showCompterNodeList();
      	/*
      	for(int s=0;s<NodeId.size();s++){
			   System.out.println(NodeId.get(s));
		    }
		    */
      	
        
	}

	
	public static float[][] readFile(String FileName) throws IOException{
		List<float[]>rList=new ArrayList<float[]>();
		RandomAccessFile inMicapsFile = null;
		String tempString;
		try {
			inMicapsFile = new RandomAccessFile(FileName, "r");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		if((tempString = inMicapsFile.readLine()) == null){
		   System.out.println("文件空:返回空");
			return null;
		}else{
			
			//System.out.println(tempString);
		}
		
		float[]v;
		boolean IsSameSize=true;
		int i=0;
		int clen=0;
		
		while ((tempString = inMicapsFile.readLine()) != null)
		{		
			
			tempString = tempString.trim();
			int index=tempString.indexOf(",");
			//System.out.println("index="+index);
			String [] values=null;
			if(index>=0){
				 values=tempString.split(",");
			}else{
				 values=tempString.split("\\s+");
			}
			
			int len2=values.length;
			if(i==0){
				clen=len2;
			}else{
				if(clen!=len2){
					System.out.println("文件第"+(i+2)+"行的有问题："+"列维度不一致");
					IsSameSize=false;
				}
			}
			if(!IsSameSize){
				return null;
			}
			v=new float[clen];
			for(int k=0;k<clen;k++){
				v[k]=Float.valueOf(values[k]);
			}
			rList.add(v);
			
		}
		
		
		try {
			inMicapsFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        int rlen=rList.size();
		  float[][]info=new float[rlen][clen];
		  for(int j=0;j<rlen;j++){
			  v=rList.get(j);
			  info[j]=v;
			 //System.out.println(" ");
			  /*
			  for(int k=0;k<clen;k++){
				  System.out.print(" "+v[k]+" ");
			  }
			  System.out.println(" ");*/
			
		
			//System.out.println(" ");
		}
		  return info;
		
	}
	
	public static List<computerNode> transform(float[][] info){
		List<computerNode> A=new ArrayList<computerNode>();
		computerNode cn;
		int size=info.length;
		for(int i=0;i<size;i++){
			float []v=info[i];
			cn=generateCN(v);
			A.add(cn);
			
		}
		return A;
	}
	public static computerNode generateCN(float[] v){
		 //System.out.println(v.length);
	 	 computerNode cn=new computerNode();
		 String s=String.format("%.0f", v[0]);
		 cn.ID=String.valueOf(s);
		 int CapaDim=4;
		 cn.Capability=new float[CapaDim];
		 int start=1;
		 for(int i=0;i<CapaDim;i++){
			 cn.Capability[i]=v[i+start];
		 }
		 int LoadDim=4;
		 start=5;
		 cn.Load=new float[4];
		 for(int i=0;i<LoadDim;i++){
			 cn.Load[i]=v[i+start];
		 }
		 cn.validAttackNum=v[9];
		 cn.totalVisitNum=v[10];
		 int HereAtrrDim=2;
		 cn.HeteroAttribute=new float[HereAtrrDim];
		 cn.AttributeType=new int[HereAtrrDim];
		 start=11;
		 int d=0;
		 for(int i=0;i<(2*HereAtrrDim-1);){
			
			 //System.out.println(i);
			 cn.HeteroAttribute[d]=v[i+start];
			 int a=(int) v[i+1+start];
			 cn.AttributeType[d]=a;
			 i=i+2;
			 d++;
		 }
		 return cn;
	}
}

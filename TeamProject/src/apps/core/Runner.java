/**
 * .class파일을 실행시켜주는 클래스
 * 수정일 : 17.11.16
 * @author 정은진
 * 
 */
package apps.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import apps.model.Model;

public class Runner {
	private Model model = Model.getInstance();
	private ArrayList<String> resultList;
	/**
	 * 
	 * 해당 파일을 실행(run)
	 */
	public ArrayList<String> run(boolean isCompiled) {
		resultList = new ArrayList<String>();
		if(!checkCompiled(isCompiled))  //잘 컴파일 됐는지 검사
			return resultList;
		try {
			String outLine, errLine;
			Process oProcess = new ProcessBuilder("java", "-cp", model.getFileDir(), model.getFileName().split(".java")[0]).start();  //cmd 명령어 ex)java -cp C:\temp\ test
			
			BufferedReader resultOut = new BufferedReader(new InputStreamReader(oProcess.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(oProcess.getErrorStream()));
			
			while ((errLine = stdError.readLine()) != null) resultList.add(errLine);
			while((outLine = resultOut.readLine()) != null)	resultList.add(outLine);
		}
		catch(IOException e) {
			resultList.add("자바 파일을 실행하지 못했습니다.\n" + e.getMessage());
		}
		return resultList;
	}
	/**
	 * 
	 * @param isCompiled:컴파일 오류가 발생한 경우 오류처리를 위한 변수
	 * @return 파일이 잘 컴파일됐었다면 true
	 * 파일이 잘 컴파일됐는지 확인 
	 */
	private boolean checkCompiled(boolean isCompiled) {
		if(!isCompiled) {  //컴파일 오류가 발생한 상태에서 run 메뉴 발생 시, boolean값 반환
			resultList.add(""+"컴파일 오류");
			return false;
		}
		if (model.getFileName().split(".java")[model.getFileName().split(".java").length -1].equals(".java")) //파일 이름이 .java 확장자가 아니라면
			return false;
		File file = new File(model.getFilePath());
		return true;
	}
}
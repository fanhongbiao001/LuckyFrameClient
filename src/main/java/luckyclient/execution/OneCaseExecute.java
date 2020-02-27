package luckyclient.execution;

import java.io.File;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

import luckyclient.execution.appium.androidex.AndroidOneCaseExecute;
import luckyclient.execution.appium.iosex.IosOneCaseExecute;
import luckyclient.execution.httpinterface.TestCaseExecution;
import luckyclient.execution.httpinterface.TestControl;
import luckyclient.execution.webdriver.ex.WebOneCaseExecute;
import luckyclient.remote.api.GetServerApi;
import luckyclient.remote.entity.TaskExecute;
import luckyclient.remote.entity.TaskScheduling;
import luckyclient.utils.LogUtil;
import luckyclient.utils.config.AppiumConfig;

/**
 * =================================================================
 * ����һ�������Ƶ��������������������κ�δ��������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ�������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸� ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944 seagull1985
 * =================================================================
 *
 * @author�� seagull
 *
 * @date 2017��12��1�� ����9:29:40
 *
 */
public class OneCaseExecute extends TestControl {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		try{
			PropertyConfigurator.configure(System.getProperty("user.dir")+ File.separator +"log4j.conf");
			String taskId = args[0];
			String caseId = args[1];
			int version = Integer.parseInt(args[2]);
			TaskExecute task = GetServerApi.cgetTaskbyid(Integer.valueOf(taskId));
			TaskScheduling taskScheduling = GetServerApi.cGetTaskSchedulingByTaskId(Integer.valueOf(taskId));
			if (taskScheduling.getTaskType() == 0) {
					// �ӿڲ���
				    TestCaseExecution testCaseExecution=new TestCaseExecution();
				    testCaseExecution.oneCaseExecuteForTask(taskScheduling.getProject().getProjectName(), Integer.valueOf(caseId), String.valueOf(task.getTaskId()));

			} else if (taskScheduling.getTaskType() == 1) {
					WebOneCaseExecute.oneCaseExecuteForTast(taskScheduling.getProject().getProjectName(), Integer.valueOf(caseId), version,
							String.valueOf(task.getTaskId()));

			} else if (taskScheduling.getTaskType() == 2) {
				Properties properties = AppiumConfig.getConfiguration();

				if ("Android".equals(properties.getProperty("platformName"))) {
					AndroidOneCaseExecute.oneCaseExecuteForTast(taskScheduling.getProject().getProjectName(), Integer.valueOf(caseId),
							version, String.valueOf(task.getTaskId()));
				} else if ("IOS".equals(properties.getProperty("platformName"))) {
					IosOneCaseExecute.oneCaseExecuteForTast(taskScheduling.getProject().getProjectName(), Integer.valueOf(caseId), version,
							String.valueOf(task.getTaskId()));
				}

			}
		}catch(Exception e){
			LogUtil.APP.error("���������������������������쳣�����飡",e);
		} finally{
			System.exit(0);
		}
	}
}
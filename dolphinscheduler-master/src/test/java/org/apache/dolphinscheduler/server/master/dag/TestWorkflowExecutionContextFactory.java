package org.apache.dolphinscheduler.server.master.dag;

import org.apache.dolphinscheduler.common.utils.CodeGenerateUtils;
import org.apache.dolphinscheduler.dao.entity.ProcessDefinition;
import org.apache.dolphinscheduler.dao.entity.ProcessInstance;

class TestWorkflowExecutionContextFactory {

    public static WorkflowExecutionContext createEmptyWorkflowExecutionContext() {
        long workflowDefinitionCode = CodeGenerateUtils.getInstance().genCode();
        int workflowDefinitionVersion = 1;

        WorkflowIdentify workflowIdentify = WorkflowIdentify.builder()
                .workflowCode(workflowDefinitionCode)
                .workflowVersion(workflowDefinitionVersion)
                .build();

        ProcessDefinition processDefinition = ProcessDefinition.builder()
                .id(1)
                .name("TestWorkflow")
                .code(workflowDefinitionCode)
                .version(workflowDefinitionVersion)
                .build();

        ProcessInstance processInstance = ProcessInstance.builder()
                .id(1)
                .name("TestWorkflowInstance")
                .build();

        return WorkflowExecutionContext.builder()
                .workflowIdentify(workflowIdentify)
                .workflowDefinition(processDefinition)
                .workflowInstance(processInstance)
                .build();
    }
}

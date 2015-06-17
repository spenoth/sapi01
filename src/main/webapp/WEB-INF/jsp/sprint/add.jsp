<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/static/js/story.form.js"></script>
</head>
<body>
    <h1><spring:message code="label.sprint.add.page.title"/></h1>
    <div class="well page-content">
        <form:form action="/sprint/add" commandName="sprint" method="POST" enctype="utf8">
            <div id="control-group-title" class="control-group">
                <label for="sprint-title"><spring:message code="label.story.title"/>:</label>

                <div class="controls">
                    <form:input id="sprint-title" path="title"/>
                    <form:errors id="error-title" path="title" cssClass="help-inline"/>
                </div>
            </div>
            <div id="control-group-description" class="control-group">
                <label for="sprint-description"><spring:message code="label.story.description"/>:</label>

                <div class="controls">
                    <form:textarea id="sprint-description" path="description"/>
                    <form:errors id="error-description" path="description" cssClass="help-inline"/>
                </div>
            </div>
            <div id="control-group-from-dt" class="control-group">
				<div class="well">
				  <div id="datetimepicker2" class="input-append">
				    <form:input data-format="MM/dd/yyyy HH:mm:ss PP" type="text" path="fromDt" />
				    <span class="add-on">
				      <i data-time-icon="icon-time" data-date-icon="icon-calendar">
				      </i>
				    </span>
				  </div>
				</div>
				<script type="text/javascript">
				  $(function() {
				    $('#datetimepicker2').datetimepicker({
				      language: 'en',
				      pick12HourFormat: false
				    });
				  });
				</script>
			</div>
			
			<div id="control-group-to-dt" class="control-group">
				<div class="well">
				  <div id="datetimepicker2" class="input-append">
				    <form:input data-format="MM/dd/yyyy HH:mm:ss PP" type="text" path="toDt" />
				    <span class="add-on">
				      <i data-time-icon="icon-time" data-date-icon="icon-calendar">
				      </i>
				    </span>
				  </div>
				</div>
				<script type="text/javascript">
				  $(function() {
				    $('#datetimepicker2').datetimepicker({
				      language: 'en',
				      pick12HourFormat: false
				    });
				  });
				</script>
			</div>
			
            <div class="action-buttons">
                <a href="/" class="btn"><spring:message code="label.cancel"/></a>
                <button id="add-story-button" type="submit" class="btn btn-primary"><spring:message
                        code="label.add.story.button"/></button>
            </div>
        </form:form>
    </div>
</body>
</html>
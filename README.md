INTRODUCTION
============

The Parameter Validation Filter (PVF) is a Java library containing a class that implements a Servlet Filter. When this class is configured in the web.xml file of a web application, it can be used to validate and sanitise parameter properties being sent to the application.

This library is based off the idea discussed on the OWASP website at https://www.owasp.org/index.php/How_to_add_validation_logic_to_HttpServletRequest.

HOW TO USE
==========

Add the following to web.xml:
```xml
	<filter>
		<filter-name>ParameterValidationFilter</filter-name>
		<filter-class>com.matthewcasperson.validation.filter.ParameterValidationFilter</filter-class>
		<init-param>
			<param-name>configFile</param-name>
			<param-value>/WEB-INF/xml/pvf.xml</param-value>
		</init-param>
	</filter>
	<filter-mapping>
		<filter-name>ParameterValidationFilter</filter-name>
		<url-pattern>*.jsp</url-pattern>
	</filter-mapping>
```	
Create a file called WEB-INF/xml/pvf.xml with the following contents:
```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<!-- ParameterValidationChainDatabase is always the document element -->
<ParameterValidationChainDatabase>
    <!--
    Enforcing mode needs to be set to true to return a HTTP error code if validation fails.
    If set to false, validation errors are logged but ignored.
    -->
    <EnforcingMode>true</EnforcingMode>
    <!-- We always have a single ParameterValidationChains element under the parent -->
    <ParameterValidationChains>
    	
    	<!-- Each chain of validation rules is contained in a ParameterValidationDefinition element -->
    	<!-- 
    		This chain apply some global validation rules. If anyone supplies encoded or params with HTML
    		characters, it will fail.
    	 -->
        <ParameterValidationDefinition>
        	<!-- This is the list of validation classes that should be applied to matching parameters -->
            <ParameterValidationRuleList>
                <ParameterValidationRule>
                	<!-- This is the fully qualified name of the class used to apply the validation rule -->
                	<!-- All input fields are to be trimmed of excess whitespace -->
                    <validationRuleName>com.matthewcasperson.validation.ruleimpl.TrimTextValidationRule</validationRuleName>
                </ParameterValidationRule>
                <ParameterValidationRule>
                	<!-- No parameters are expected to already be encoded -->
                    <validationRuleName>com.matthewcasperson.validation.ruleimpl.FailIfNotCanonicalizedValidationRule</validationRuleName>
                </ParameterValidationRule>
                <ParameterValidationRule>
                	<!-- No parameters are expected to contain html -->
                    <validationRuleName>com.matthewcasperson.validation.ruleimpl.FailIfContainsHTMLValidationRule</validationRuleName>
                </ParameterValidationRule>
            </ParameterValidationRuleList>
            <!-- This is a regex that defines which parameteres will be validated by the classes above -->
            <paramNamePatternString>.*</paramNamePatternString>
            <!-- This is a regex that defines which URLs will be validated by the classes above -->
            <requestURIPatternString>.*</requestURIPatternString>
            <!--
            	 Setting this to false means the paramNamePatternString has to match the param name.
            	 Setting it to true would mean that paramNamePatternString would have to *not* match the param name.
             -->          
            <paramNamePatternNegated>false</paramNamePatternNegated>
            <!--
            	 Setting this to false means the requestURIPatternString has to match the uri.
            	 Setting it to true would mean that requestURIPatternString would have to *not* match the uri name.
             -->
            <requestURIPatternNegated>false</requestURIPatternNegated>
        </ParameterValidationDefinition>        

        
    </ParameterValidationChains>
</ParameterValidationChainDatabase>
```

FLOWCHART
=========

![flowchart](https://raw.githubusercontent.com/AutoGeneral/ParameterValidationFilter/master/flowchart.png)

DEMO
====

Go to http://pvftest-matthewcasperson.rhcloud.com/ to see the parameter validation filter is action.

The source for the demo can be found at https://github.com/mcasperson/ParameterValidationFilterDemo.

MORE INFORMATION
================

See https://www.owasp.org/index.php/Parameter_Validation_Filter.

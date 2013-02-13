@REM
@REM  Copyright (C) 2013 mgm technology partners GmbH, Munich.
@REM
@REM  See the LICENSE file distributed with this work for additional
@REM  information regarding copyright ownership and intellectual property rights.
@REM

@ECHO OFF

CALL setenv.bat

ECHO Using JAVA_HOME:   %JAVA_HOME%
ECHO Using JAVA_OPTS:   %JAVA_OPTS%
ECHO Using APP_OPTS:    %APP_OPTS%

CALL %EXEC_CMD% %JAVA_OPTS% %APP_OPTS% -cp ./config;./lib/jfunk-core-%JFUNK_VERSION%.jar com.mgmtp.jfunk.core.JFunk -threadcount=4 %*

PAUSE

IF NOT "%EXIT_AFTER_RUNNING%" == "true" GOTO end

EXIT

:end

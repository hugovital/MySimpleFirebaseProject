*** Settings ***
Library           AppiumLibrary

*** Variables ***
${APP}           app-debug.apk
${PLATFORM}      Android
${DEVICE_NAME}    emulator-5554
${APP_PACKAGE}    com.hugo.mysimplefirebaseproject
${APP_ACTIVITY}   .MainActivity

*** Test Cases ***
Test Cat Fact Fetch
    [Documentation]    Test fetching a cat fact and displaying it
    Open Application   http://localhost:4723/wd/hub  platformName=${PLATFORM}  deviceName=${DEVICE_NAME}  app=${APP}  appPackage=${APP_PACKAGE}  appActivity=${APP_ACTIVITY}
    Click Element      id=buttonClick
    Sleep              5s
    ${fact}=           Get Text    id=catFactTextView
    Log                Cat Fact: ${fact}
    Close Application

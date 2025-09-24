rem pack200 -J-Xmx512m --repack b.jar TTUi.jar
rem jarsigner -keystore cbms.jks -storepass cbmsfin -keypass cbmsfin b.jar cbms

rem pack200 -J-Xmx512m --repack TTUi.jar b.jar
rem jarsigner -keystore cbms.jks -storepass cbmsfin -keypass cbmsfin -signedjar signedTTUI.jar TTUI.jar cbms
 
rem pack200 -J-Xmx512m signedTTUI.jar.pack.gz signedTTUI.jar

jarsigner -keystore cbms.jks -storepass cbmsfin -keypass cbmsfin -signedjar signedTTUI.jar TTUI.jar cbms
 

copy TTServer.jar E:\TTCBS_KERALA\jboss-as-7.1.1.Final\standalone\deployments\arcat.ear\TTServer.jar
rem copy signedTTUI.jar.pack.gz E:\TTCBS_KERALA\jboss-as-7.1.1.Final\standalone\deployments\arcat.ear\cbms.war\signedTTUI.jar.pack.gz
copy signedTTUI.jar E:\TTCBS_KERALA\jboss-as-7.1.1.Final\standalone\deployments\arcat.ear\cbms.war\signedTTUI.jar

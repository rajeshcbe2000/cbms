pack200 -J-Xmx512m --repack b.jar TTUi.jar
jarsigner -keystore cbms.jks -storepass cbmsfin -keypass cbmsfin b.jar cbms

pack200 -J-Xmx512m --repack TTUi.jar b.jar
jarsigner -keystore cbms.jks -storepass cbmsfin -keypass cbmsfin -signedjar signedTTUI.jar TTUI.jar cbms
 
pack200 -J-Xmx512m signedTTUI.jar.pack.gz signedTTUI.jar

rem jarsigner -keystore cbms.jks -storepass cbmsfin -keypass cbmsfin -signedjar signedTTUI.jar TTUI.jar cbms
 

copy signedTTUI.jar.pack.gz C\CBMS\jboss-as-7.1.1.Final\standalone\deployments\arcat.ear\cbms.war\signedTTUI.jar.pack.gz
copy signedTTUI.jar C:\CBMS\jboss-as-7.1.1.Final\standalone\deployments\arcat.ear\cbms.war\signedTTUI.jar

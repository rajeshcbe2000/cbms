/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  nithya
 * Created: Jul 25, 2025
 */

SET DEFINE OFF;
Insert into CBMS_PARAMETERS
(CBMS_KEY, CBMS_VALUE, DESCRIPTION)
Values
('WEBCAM_INTEGRATION', 'Y', NULL);
COMMIT;


UPDATE LOANS_PRODUCT SET GOLD_STOCK_PHOTO_REQUIRED = 'Y' where PROD_ID = prod_id



 --Create a folder webcam in customer folder in server path 

           --D:\TTCBS7.1.1\jboss-as-7.1.1.Final\standalone\deployments\arcat.ear\cbms.war\customer\webcam



--Path for deploying signed jar files

--E:\TTCBS7.1.1\TTCBS7.1.1\jboss-as-7.1.1.Final\standalone\deployments\arcat.ear\cbms.war



job("J1_groovy"){
	  description("kubernetes job1")
	  scm{
	    github('Neelaksh-Singh/devopsal6','master')
	  }
	  steps{
	    shell('sudo cp -vrf * /projects/jenkins')
	  }
	  triggers{
	    gitHubPushTrigger()
	  }
	
}

job("J2_groovy"){
	  steps{
	    shell('''
		if sudo ls /projects/jenkins | grep php
	      	then
			if sudo kubectl get deployment --selector "app in (httpd)" | grep httpd-web
	    		then
				sudo kubectl apply -f /projects/jenkins/web.yml
	           		POD=$(sudo kubectl get pod -l app=httpd -o jsonpath="{.items[0].metadata.name}")
	        		echo $POD
	        		sudo kubectl cp /projects/jenkins/index.php $POD:/var/www/html
			else
	    			if ! kubectl get pvc | grep httpdweb1-pv-claim
	        		then
	            			sudo kubectl create -f /projects/jenkins/pvc.yml
	        		fi
	        		sudo kubectl create -f /projects/jenkins/web.yml
	        		POD=$(sudo kubectl get pod -l app=httpd -o jsonpath="{.items[0].metadata.name}")
	        		echo $POD
	        		sudo kubectl cp /projects/jenkins/index.php $POD:/var/www/html
	    		fi
	   	fi
		''')
	  }
	  triggers {
	        upstream('J1_groovy', 'SUCCESS')
	  }
}

job("J3_groovy")
	{
	  steps{
	    shell('''
	status=$(curl -o /dev/null -s -w "%{http_code}" http://192.168.99.100:30002)
	if [[ $status == 200 ]]
	then
	    echo "Running"
	    exit 0
	else
	     exit 1
	fi
	     ''')
	  }
	  
	  triggers {
	        upstream('J2_groovy', 'SUCCESS')
	  }
	  
	  publishers {
	        extendedEmail {
	            recipientList('neelaksh48@gmail.com')
	            defaultSubject('Job status')
	          	attachBuildLog(attachBuildLog = true)
	            defaultContent('Status Report')
	            contentType('text/html')
	            triggers {
	                always {
	                    subject('build Status')
	                    content('Body')
	                    sendTo {
	                        developers()
	                        recipientList()
	                    }
			       }
		       }
		   }
	  }
 

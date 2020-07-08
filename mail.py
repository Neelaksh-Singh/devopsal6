import smtplib
import urllib.request as urllib
# Senders email
sender_email = "neelaksh481@gmail.com"
# Receivers email
rec_email = "neelaksh481@gmail.com"

message = "Kuberenetes cluster not running.......Do the required changes."
# Initialize the server variable
server = smtplib.SMTP('smtp.gmail.com', 587)
# Start the server connection
server.starttls()
# Login
server.login("neelaksh481@gmail.com", "Password")
print("Login Success!")
# Send Email
server.sendmail("Neelaksh Singh", "neelaksh481@gmail.com", message)
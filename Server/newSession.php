<?php
/*
newSession - Creates a new session
Params: Session Name, password (can be blank), owner
Returns: success (owner+name unique)
         error (owner+name not unique)
           */
include 'getUser.php';
  
$token = $_GET["token"];
$user_id = getUserFromToken($token);
  
$session_name = $_GET["session_name"];
$passcode = $_GET["passcode"];

if($passcode=NULL) {
  $query = "INSERT INTO sessions (name, owner) VALUES('$session_name','$user_id')";
}
else {
  $query = "INSERT INTO sessions (name, passcode, owner) VALUES('$session_name','$passcode','$user_id')";
}
  
$result = $mysqli->query($query);  

if($result==false) {
  error_match("CREATE_SESSION_FAILURE");
  exit;
}
else {
  $query = "INSERT INTO in_session (user, session) VALUES ('$user_id','$mysqli->insert_id')"
  $result = $mysqli->query($query);  

  if($result==false) {
    error_match("ENROLL_USER_FAILURE");
    exit;
  }
}
echo "Success!";
  
?>
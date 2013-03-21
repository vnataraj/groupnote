<?php
/*
newSession - Creates a new session
Params: Session Name, password (can be blank), owner
Returns: success (owner+name unique)
         error (owner+name not unique)
           */
include 'database.php';
  
$token = $_GET["token"];

$user_id = validate($token);
if($user_id==-1) {
  $error = "NOT_LOGGED_IN";
  include 'error.php';
  exit;
}
  
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
  $error = "CREATE_SESSION_FAILURE";
  include 'error.php';
}
else {
  $query = "INSERT INTO in_session (user, session) VALUES ('$user_id','$mysqli->insert_id')"
  $result = $mysqli->query($query);  

  if($result==false) {
    $error = "ENROLL_USER_FAILURE";
    include 'error.php';
  }
}

//success! We should probably tell them
  
?>
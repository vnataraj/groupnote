<?php
/*
joinSession - Creates a new session
Params: Session number, password
Returns: success (owner+name unique)
         error (owner+name not unique)
           */
include 'database.php';
  
$token = $_GET["token"];
$session_id = $GET["session_id"];

$user_id = validate($token);
if($user_id==-1) {
  $error = "NOT_LOGGED_IN";
  include 'error.php';
  exit;
}

//query for session password
$query = "SELECT passcode FROM sessions WHERE session_id=$session_id";

$result = $mysqli->query($query);

if($result) {
  $row = $result->fetch_assoc();
  //create the passcode hash, but we'll use plaintext for now
  if(row['passcode']!=NULL) {
    if($passcode!=$row['passcode']) {
      $error = "INVALID_PASSWORD";
      include 'error.php';
      exit;
    }
  }
  //enroll user in session
  $query = "INSERT INTO in_session (user, session) VALUES('$user_id','$session_id')";
  
  $result = $mysqli->query($query);
  if(!$result) {
    $error = "MYSQL_ERROR";
    include 'error.php';
    exit;
  }
}

?>
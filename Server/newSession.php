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
$latitude = $_GET["latitude"];
$longitude = $_GET["longitude"];
$radius = $_GET["radius"];

$query_head = "INSERT INTO sessions (name";
$query_tail = "VALUES('$session_name'";

if(strcmp($passcode,"")!=0) {
  $query_head = $query_head . ",passcode";
  $query_tail = $query_tail . ",'$passcode'";
}
if(strcmp($latitude,"")!=0) {
  $query_head = $query_head . ",latitude";
  $query_tail = $query_tail . ",'$latitude'";
}
if(strcmp($longitude,"")!=0) {
  $query_head = $query_head . ",longitude";
  $query_tail = $query_tail . ",'$longitude'";
}
if(strcmp($radius,"")!=0) {
  $query_head = $query_head . ",radius";
  $query_tail = $query_tail . ",'$radius'";
}

$query_head = $query_head . ") ";
$query_tail = $query_tail . ")";

//$query = "INSERT INTO sessions (name,passcode,latitude,longitude,radius) VALUES('$session_name','$passcode','$latitude','$longitude','$radius')";
$query = $query_head . $query_tail;
//echo $query;

/*if(strcmp($passcode,"")==0) {
  //echo "passcode null...inserting without<br>";
  $query = "INSERT INTO sessions (name) VALUES('$session_name')";
}
else {
  //echo "passcode was not null...inserting with<br>";
  $query = "INSERT INTO sessions (name, passcode) VALUES('$session_name','$passcode')";
}
*/
//echo "query is $query";
$result = $mysqli->query($query);  

if($result==false) {
  echo FAILURE;
  //error_match("CREATE_SESSION_FAILURE");
  exit;
}
else {
  $new_session_id = $mysqli->insert_id;
  $query = "INSERT INTO in_session (user, session, is_owner) VALUES ('$user_id','$new_session_id','1')";
  $result = $mysqli->query($query);  

  if($result==false) {
    //error_match("ENROLL_USER_FAILURE");
    echo FAILURE;
    exit;
  }
  else {
    echo $new_session_id;
  }
}
  
?>
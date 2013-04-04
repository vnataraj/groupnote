<?php

include 'getUser.php';
  
$username = $_GET["username"];
$password = $_GET["password"];

$token = getTokenFromUser($username,$password);
if($token!=-1) { 
  echo $token;
}
else {
  echo "-1";
}
?>
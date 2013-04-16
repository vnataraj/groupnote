<?php

include 'getUser.php';

$token = $_GET["token"];
  
$query = "SELECT refresh_time FROM users WHERE token='$token'";

$result=$mysqli->query($query);

if($result) {
  $row = $result->fetch_assoc();
  if($row['refresh_time']) {
    echo $row['refresh_time'];
  }
  else {
    echo FAILURE;
  }
} 
else {
  echo FAILURE;
}

?>
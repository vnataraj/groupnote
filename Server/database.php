<?php
  $DB_NAME = "a6918140_group";
  $DB_HOST = "mysql2.000webhost.com";
  $DB_USER = "a6918140_group";
  $DB_PASS = "307groupnote";

  // Create connection
  $mysqli=mysqli_connect($DB_HOST,$DB_USER,$DB_PASS,$DB_NAME);
  
  // Check connection
  if (mysqli_connect_errno($mysqli)) {
    echo "Failed to connect to MySQL: " . mysqli_connect_error();
    exit;
  }
  
//takes a user token as input and returns the user ID if the token exists
//returns -1 if no token is found in the database
function validate($user_token) {
  $result = $mysqli->query("SELECT user_id FROM users WHERE token=$user_token");
  $user_id_no = -1;
  if($result) {
    $row = $result->fetch_assoc();
    $user_id_no = $row['user_id'];
  }
  return $user_id_no;
}
?>
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
?>
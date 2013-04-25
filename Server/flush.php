<?php

include 'getUser.php';

$query = "TRUNCATE TABLE `in_session`";
$mysqli->query($query);

$query = "TRUNCATE TABLE `notes`";
$mysqli->query($query);

$query = "TRUNCATE TABLE `sessions`";
$mysqli->query($query);

$query = "TRUNCATE TABLE `users`";
$mysqli->query($query);

?>
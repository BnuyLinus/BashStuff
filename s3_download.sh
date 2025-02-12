#!/bin/bash

# this script was part of my final project to qualify as an IT specialist for system integration
# P.S. I passed


# Define Log file for script
LOG_FILE="/home/#USER/project/log/s3_download.log"

# Use AWS CLI to retrieve file from S3-Bucket
# Bucket address has been removed
aws s3 cp s3://#BUCKET NAME HERE/capture.png /home/#USER/project/bucket/

# Check if S3 copy was successful
if [ $? -eq 0 ]; then
    echo "$(date +'%Y-%m-%d %T'): File copied successfully from S3-Bucket. Exit Code 0 from AWS-CLI." >> $LOG_FILE
else
    echo "$(date +'%Y-%m-%d %T'):Failed to copy file from S3-Bucket." >> $LOG_FILE
    exit 1
fi

# Check if feh is running already
process=$(ps -Af | grep feh | grep capture.png)

# Open the image on display
if [ -z "$process" ]; then
    DISPLAY=:0 feh --fullscreen /home/#USER/project/bucket/capture.png
fi
#!/bin/bash
# Checks if AWS CLI is installed on your system
if ! command -v aws &> /dev/null; then
        echo "AWS CLI is not responding. It is likely that the AWS CLI is not installed or faulty. Aborting..."
        exit 1
fi
        echo "AWS CLI is installed and respondive."

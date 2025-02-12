#!/bin/bash
# This script was part of a learning project in wish I set up an AWS environment and hosted a database backed web-server running a serndipity blog
# This script backs up the contents of the database routinely.
datum=$(date +%F)
output="/root/backups/serendipity-db-backup-${datum}.sql"


mkdir -p $(dirname $output)
mysqldump -u root --lock-tables --routines --databases serendipity > $output
if [ -f "${output}" ]; then
  bzip2 "${output}"
  if [ -f "${output}.bz2" ]; then
    aws s3 cp "${output}.bz2" s3://#BUCKET NAME HERE!/serendipity-backups/
    if [ $? -ne 0 ]; then
      >&2 echo "Upload of ${output}.bz2 to S3 Bucket thorben-exercise failed."
    fi
  else
    >&2 echo "backup failed. ${output}.bz2 does not exist."
    exit 255
  fi
else
  >&2 echo "backup failed. ${output} does not exist."
  exit 255
fi

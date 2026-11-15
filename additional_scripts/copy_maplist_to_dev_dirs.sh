#!/bin/bash

# Configuration
SCRIPT_PATH=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )
cd $SCRIPT_PATH || exit
cd ../
SOURCE_FILE="shared_resources/maps.yaml"
TARGET_DIRS=("dashboard/shared_resources" "data_controller/shared_resources" "discord_bot/shared_resources" "google_api_handler/shared_resources" "tracker/shared_resources")

# Check if source file exists
if [[ ! -f "$SOURCE_FILE" ]]; then
    echo "Error: Source file '$SOURCE_FILE' not found!"
    exit 1
fi

echo "Copying $SOURCE_FILE to directories..."

for dir in "${TARGET_DIRS[@]}"; do
    # Create directory if it doesn't exist
    mkdir -p "$dir"

    # Copy with overwrite (force)
    cp -f "$SOURCE_FILE" "$dir/"

    echo "✓ Copied to $dir/"
done

echo "Done!"

from playwright.sync_api import sync_playwright
import boto3
from botocore.exceptions import ClientError
import random

DEBUG=True
urllist = [ URL_LIST array here ]

def run(playwright,url):
    # launch the browser
    browser = playwright.firefox.launch(headless=True)
    # opens a new browser page
    page = browser.new_page()
    # navigate to the website
    page.goto(url)
    # take a full-page screenshot
    page.screenshot(path='capture.png', full_page=False)
    # always close the browser
    browser.close()


if __name__ == '__main__':

    # get screenshot
    with sync_playwright() as playwright:
        run(playwright,random.choice(urllist))
    # upload screenshot to s3 bucket
    s3_client = boto3.client('s3')
    response = s3_client.upload_file('capture.png', 'BUCKET-NAME-HERE', 'capture.png')

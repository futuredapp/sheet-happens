# Generating Google Sheets API key

TLDR: Use Google Cloud project developer console to generate an API key restricted to Sheets API.
Make your Google Sheet visible to anyone with the link.

1. Go to [Google Cloud console](https://console.cloud.google.com/) and select your project, or create a new one
2. In `APIs & Services`, enable the `Google Sheets API`
3. In `APIs & Services > Credentials`, generate new API key. It's a good practice to restrict that key, so restrict it to Google Sheets API you just enabled.
4. Voilà, you just got yourself an API key. 🎉
5. Finally, you need to be able to access your Google Sheet using this API key. The easiest way to do that is to make your Google Spreadsheet accessible publicly for reading to anyone with the link.
# Caddy
The bot designed for the Cadence discord server

## Running
> [!NOTE]
> 
> Make sure you have Git and Java installed

1. Clone this repo
    - `git clone https://github.com/cadence-app/Caddy.git && cd gloom`
2. Set up environment variables
    - `CADDY_BOT_TOKEN`: The token for your bot, click [here](https://discord.com/developers/docs/quick-start/getting-started#step-1-creating-an-app) to find out how to obtain your bot's token
3. Build the project
    - Linux: `chmod +x ./gradlew && ./gradlew build`
    - Windows: `./gradlew build`
4. Run the bot
    - `java -jar build/libs/Caddy.jar`

## License
Caddy is licensed under the MIT license
```
MIT License

Copyright (c) 2024 Cadence

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
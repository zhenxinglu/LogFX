<div id="top"></div>


<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]



<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/zhenxinglu/LogFX">
    <img src="images/logfx.png" alt="Logo" width="100" height="100">
  </a>

<h3 align="center">LogFX</h3>

  <p align="center">
    A tiny tool to review and filter Log4j logs in multiple files!
    <br />
    <a href="https://github.com/zhenxinglu/LogFX"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/zhenxinglu/LogFX">View Demo</a>
    ·
    <a href="https://github.com/zhenxinglu/LogFX/issues">Report Bug</a>
    ·
    <a href="https://github.com/zhenxinglu/LogFX/issues">Request Feature</a>
  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#build-the-installer">Build the installer</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

[![Product Name Screen Shot][product-screenshot]](https://gluonhq.com/products/javafx/)

Do your Log4j logs spreading in different files? Are your logs files located in different servers? 
Do the modules have interactions but the logs are in different files so you can not find out the 
message flows between them? If so, please try LogFX. It provides a fancy solution to address these 
issues. :smile:


<p align="right">(<a href="#top">back to top</a>)</p>


### Built With

LogFX is a JavaFX application. It ues the scp function of ant-jsch to copy the files from remote servers.  

* [JavaFX](https://openjfx.io/)
* [ant-jsch](https://mvnrepository.com/artifact/org.apache.ant/ant-jsch)

<p align="right">(<a href="#top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started

Log4j is built with Maven. Because it builds the custom JRE as well so the local JavaFX 23 modues must 
be installed and the relevant environment values should be correctly setup.

### Prerequisites
* install [JavaFX 23+ modules](https://gluonhq.com/products/javafx) and set environment variable PATH_TO_FX_MODS.
  ```sh
  PATH_TO_FX_MODS=/path/to/JavaFX/modules
  ```
* install Maven
* install JDK 23+

### Build the installer
1. cd <project_home_dir>
2. ./build.sh

After the build we can find the LogFX installer for Windows in the `output` directory under the home directory
of the project. 

<p align="right">(<a href="#top">back to top</a>)</p>



<!-- USAGE EXAMPLES -->
## Usage
User can launch LogFX from the desktop or Windows menu, then view the log:

* fill the file path of the logs files.
    * for local file, just put the file path, like:   `F:\\tmp\\my.log`
    * for remote file, input: `ssh://<user>:<password>@<host>[:<port>]<log_file_path>`
    
        e.g. `ssh://root:root12@10.10.0.29:22/tmp/mylog.log`
        if the port is not specifed, default SSH port 22 is used: `ssh://root:root12@10.10.0.29/tmp/mylog.log` 
  
* fill the key words
  <p/>
    If the key word contains space, quote it with "". As soon as one of the specified key word
    is found the log will be displayed.
* fill the exclude words
  If the key word contains space, quote it withu "". As soon as one of the specified key word
  is found the log will NOT be displayed.
* fill the time range of the logs
* click `Show Logs` to display the logs. The logs are sorted with the timestamp.

All the filter conditions and log files information are saved when the application is closed, so the user
can quickly starts from where they leave the application.

<p align="right">(<a href="#top">back to top</a>)</p>



<!-- ROADMAP -->
## Roadmap

- [x] First workable version
- [ ] Add GUI for add/remove the local or remote log files
- [ ] Multi-language Support
    - [ ] Chinese
    - [ ] English

See the [open issues](https://github.com/zhenxinglu/LogFX/issues) for a full list of proposed features (and known issues).

<p align="right">(<a href="#top">back to top</a>)</p>



<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<p align="right">(<a href="#top">back to top</a>)</p>


<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#top">back to top</a>)</p>


<!-- CONTACT -->
## Contact

Zhenxing Lu : zhenxinglu@gmail.com

Project Link: https://github.com/zhenxinglu/LogFX

<p align="right">(<a href="#top">back to top</a>)</p>


<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/othneildrew/Best-README-Template.svg?style=for-the-badge
[contributors-url]: https://github.com/zhenxinglu/LogFX/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/othneildrew/Best-README-Template.svg?style=for-the-badge
[forks-url]: https://github.com/zhenxinglu/LogFX/network/members
[stars-shield]: https://img.shields.io/github/stars/othneildrew/Best-README-Template.svg?style=for-the-badge
[stars-url]: https://github.com/zhenxinglu/LogFX/stargazers
[issues-shield]: https://img.shields.io/github/issues/othneildrew/Best-README-Template.svg?style=for-the-badge
[issues-url]: https://github.com/zhenxinglu/LogFX/issues
[license-shield]: https://img.shields.io/github/license/othneildrew/Best-README-Template.svg?style=for-the-badge
[license-url]: https://github.com/zhenxinglu/LogFX/blob/master/LICENSE.txt
[product-screenshot]: images/logfx_screenshot.png

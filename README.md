# QR Generator
<b>Simple application to generate QR codes for logging in to any Wi-Fi network. </b>

<p>To use the application, simply run the main method in the GenerarQR class. The application will prompt you to enter the SSID and password of the Wi-Fi network you want to generate a QR code for
Once you have entered the required information, the application will generate a QR code that can be scanned by any device to connect to the Wi-Fi network.</p>

<b>Note:</b> This application uses the ZXing library to generate QR codes. Make sure to include the ZXing library in your project dependencies to run the application successfully.
## <b>Example:</b>
```
SSID: MyWiFiNetwork
Password: MySecurePassword
```

### In progress:
- [ ] Add error handling for invalid input.
- [ ] Implement a graphical user interface (GUI) for easier use.
- [ ] Allow users to use any .pdf they want, not only the one in the project folder.
- [ ] Add functionality to put the QR code where you want in the .pdf, not only in the position it's currently in.
- [ ] Add functionality to customize the size of the QR code.

### Known issues:
- The application currently does not support any other pdf files than the one in the project folder. This is a limitation that will be addressed in future updates.
- If the graphical user interface may be poorly designed, maybe a little bit ugly, it's not really an issue but it can be improved in the future.

### Contact:
If you have any questions or suggestions, feel free to contact me at [kacperjasinskibusiness@gmail.com].


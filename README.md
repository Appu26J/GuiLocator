<img width="360" alt="image" src="https://github.com/Appu26J/GuiLocator/assets/128838345/05f5c19a-f8f2-4966-8f33-1007121d2757">

A Java library which allows you to locate images on another image/the screen.  
This is basically equivalent to Python's pyautogui which has locate, locateOnScreen, and click!

### Example Usage
```java
int[] image = GuiLocator.locateOnScreen(new File("image.png"));
GuiLocator.leftClickAt(image);
```

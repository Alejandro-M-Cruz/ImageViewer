package Skin;

import Architecture.ImageDisplay;
import Architecture.ImagePresenter;
import Architecture.NextCommand;
import Architecture.PrevCommand;
import java.io.File;

public class Main {

    public static void main(String[] args) {
        FileImageLoader loader = new FileImageLoader(new File("images"));        
        MainFrame mainFrame = new MainFrame();        
        ImageDisplay imageDisplay = mainFrame.imageDisplay();
        ImagePresenter presenter = ImagePresenter.with(loader.load(), imageDisplay);        
        mainFrame.addCommand("prev", new PrevCommand(presenter));
        mainFrame.addCommand("next", new NextCommand(presenter));
        mainFrame.setVisible(true);
    }
}

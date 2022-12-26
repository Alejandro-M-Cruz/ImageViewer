package Skin;

import Architecture.Command;
import Architecture.ImageDisplay;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MainFrame extends JFrame {

    private final ImagePanel imagePanel;
    private final Map<String,Command> commands;
    private final JLabel imageName;

    public MainFrame() {
        super("Image Viewer");
        commands = new HashMap();
        this.setSize(800,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.add(imagePanel = new ImagePanel(), BorderLayout.CENTER);
        this.add(button("prev","<"), BorderLayout.WEST);
        this.add(button("next",">"), BorderLayout.EAST);
        this.add(imageName = new JLabel(), BorderLayout.NORTH);
        imageName.setHorizontalAlignment(SwingConstants.CENTER);
    }
    
    public ImageDisplay imageDisplay() {
        return imagePanel;
    }

    private JButton button(String name, String label) {
        JButton button = new JButton(label);
        button.setEnabled(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                commands.get(name).execute();
            }
        });
        return button;
    }

    public void addCommand(String name, Command command) {
        commands.put(name, command);
    }
        
    private void updateImageName(String name) {
        imageName.setText(name);
    }
    
    private class ImagePanel extends JPanel implements ImageDisplay {
        
        private final List<Order> orders;
        private DragEvent onDragged = DragEvent::Null;
        private NotifyEvent onReleased = NotifyEvent::Null;
        private int x;

        private ImagePanel() {
            this.orders = new ArrayList<>();
            this.addMouseListener(new MouseListener() {
                
                @Override
                public void mouseClicked(MouseEvent e) {}

                @Override
                public void mousePressed(MouseEvent e) {
                    x = e.getX();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    onReleased.handle(e.getX()-x);
                }

                @Override
                public void mouseEntered(MouseEvent e) {}

                @Override
                public void mouseExited(MouseEvent e) {}
            });
            
            this.addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    onDragged.handle(e.getX()-x);
                }

                @Override
                public void mouseMoved(MouseEvent e) {}
            });
        }

        @Override
        public void paint(Graphics g) {
            clean(g);
            for (Order order : orders) {
                order.calculate(this.getWidth(), this.getHeight());
                g.drawImage(order.image, order.x(this.getWidth()), order.y(this.getHeight()), order.width, order.height, null);                
            }
        }

        private void clean(Graphics g) {
            g.setColor(Color.white);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }

        @Override
        public void clear() {
            orders.clear();
        }

        @Override
        public void paint(Object data, int offset) {
            orders.add(new Order(data, offset));
            repaint();
        }
        
        @Override
        public void onDragged(DragEvent event) {
            this.onDragged = event;
        }

        @Override
        public void onReleased(NotifyEvent event) {
            this.onReleased = event;
        }

        @Override
        public int width() {
            return this.getWidth();
        }
        
        @Override
        public void updateName(String name) {
            updateImageName(name);
        }
    }

    private static class Order {
        private final BufferedImage image;
        private final int offset;
        private int width;
        private int height;
        private final double ratio;

        public Order(Object data, int offset)  {
            this.image = (BufferedImage) data;
            this.offset = offset;
            this.width = image.getWidth();
            this.height = image.getHeight();
            this.ratio = (double) width / height;
        }
        
        public void calculate(int panelWidth, int panelHeight) {
            width = image.getWidth();
            height = image.getHeight();
            if(height > panelHeight) {
                height = panelHeight;
                width = (int) (height * ratio);
            } 
            if(width > panelWidth) {
                width = panelWidth;
                height = (int) (width / ratio);
            }
        }
        
        public int x(int width) {
            return (width - this.width) / 2 + offset;
        }
        
        public int y(int height) {
            return (height - this.height) / 2;
        }
    }
}
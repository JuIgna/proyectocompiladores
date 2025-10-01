package proyectocompiladores;

import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.tree.ParseTree;

import javax.swing.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Arrays;
import java.util.List;

public class ArbolAST {
    
    private JFrame frame;
    private JScrollPane scrollPane;
    private TreeViewer viewer;
    private double scale = 1.0;

    public ArbolAST(ParseTree tree, String[] ruleNames) {
        List<String> reglas = Arrays.asList(ruleNames);
        viewer = new TreeViewer(reglas, tree);
        viewer.setScale(scale);
        scrollPane = new JScrollPane(viewer);

        scrollPane.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.isControlDown()) {
                    if (e.getWheelRotation() < 0) {
                        scale *= 1.1; 
                    } else {
                        scale /= 1.1; 
                    }
                    viewer.setScale(scale);
                    frame.revalidate();
                }
            }
        });

        frame = new JFrame("Árbol de análisis");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(scrollPane);
        frame.setSize(1000, 800);
    }

    public void mostrar() {
        frame.setVisible(true);
    }

}

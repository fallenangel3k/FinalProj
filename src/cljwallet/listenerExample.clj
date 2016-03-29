(import '(javax.swing JFrame JPanel JButton))


(def button (JButton. "Click Me!"))
(def panel (doto (JPanel.)
             (.add button)))
(def frame (doto (JFrame. "Hello Frame")
             (.setSize 200 200)
             (.setContentPane panel)
             (.setVisible true)))

(import 'javax.swing.JOptionPane)
(defn say-hello []
     (JOptionPane/showMessageDialog nil "Hello world!" "Greeting" JOptionPane/INFORMATION_MESSAGE))

(import 'java.awt.event.ActionListener)
(def act (proxy [ActionListener] []
           (actionPerformed [event] (say-hello))))

(.addActionListener button act)

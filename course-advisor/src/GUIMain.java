import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * @author Emery
 */
public class GUIMain extends javax.swing.JFrame {
    private CourseList cl = new CourseList();
    private RuleList rl = new RuleList();
    private InferenceEngine ie;
    private Session session;
    
    /**
     * Creates new form NewJFrame
     */
    public GUIMain() { 
        cl.loadCourseList();
        rl.loadRuleList("cpscrules.txt");
        initComponents();
        initTableModel();
        initRuleComponents();    
        this.setResizable(false);
    }
    
    /**
     * Initialize scorecard components
     * // \u2713 (checkmark) \u2717 (x) \u03B8 (Theta)
     */
    private void initRuleComponents(){
        // Add rules to the Scorecard
        jPanel2.setLayout(new GridLayout(0,2));
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        int i = 0;
        for(Rule rule : rl.getRuleSetArray()){
            i++;
            c.gridx = i % 2;
            c.gridy = (int) (i / 2);
            //Setup rule panel
            JPanel rulePanel = new JPanel(); 
            rulePanel.setLayout(new GridBagLayout());
            JLabel lbl = new JLabel(rule.getName());
            rulePanel.add(lbl);
            JLabel scorelbl = new JLabel("      \u03B8"); 
            rulePanel.add(scorelbl);
            jPanel2.add(rulePanel, c);
        }
    }
    
    /**
     * Display if the rule is satisfied or not
     */
    private void setRulesSatisfied(){
        jPanel2.removeAll();
        // Add rules to the Scorecard
        jPanel2.setLayout(new GridLayout(0,2));
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        int i = 0;
        for(Rule rule : rl.getRuleSetArray()){
            i++;
            c.gridx = i % 2;
            c.gridy = (int) (i / 2);
            boolean isSatisfied = rule.check(ie.getFacts().keySet().toArray(new String[ie.getFacts().keySet().size()]));
            //Setup rule panel
            JPanel rulePanel = new JPanel(); 
            rulePanel.setLayout(new GridBagLayout());
            JLabel lbl = new JLabel(rule.getName());
            rulePanel.add(lbl);
            JLabel scorelbl = new JLabel((isSatisfied)?"      \u2713":"       \u2717"); 
            scorelbl.setForeground((isSatisfied) ? new Color(34,139,34,255): Color.red);
            Font newLabelFont=new Font(scorelbl.getFont().getName(),Font.BOLD,scorelbl.getFont().getSize());  
            scorelbl.setFont(newLabelFont);
            rulePanel.add(scorelbl);
            jPanel2.add(rulePanel, c);
        }
        jPanel2.updateUI();
    }
    
    /**
     * @return Number of rows for the Scorecard gridrow layout
     */
    private int getRuleRows(){
        return (int)Math.ceil(rl.getRuleSetArray().size()/2);
    }
    
    /**
     * Loads and binds the courses to the taken course list
     * @return the listmodel with the courses stored in it
     */
    public DefaultListModel loadCourses() {
        DefaultListModel<String> model = new DefaultListModel();
        
        if(cl.loadCourseList()) {
            String[] courses = cl.getCourseNames();
            Arrays.sort(courses);
            for(String course: courses) 
                model.addElement(course);
        }
        return model;
    }
    
    /**
     * Loads and binds the Interests (Degrees Ex CPSC, ANTH) to the interests course list
     * @return the listmodel with the degree four-letter acronym stored in it
     */
    public DefaultListModel loadInterests(){
        DefaultListModel<String> model = new DefaultListModel();
        if(cl.loadCourseList()) {
            String[] courses = cl.getCourseNames();
            Arrays.sort(courses);
            for(String course: courses) 
                if(model.indexOf(course.substring(0, 4)) == -1)
                    model.addElement(course.substring(0, 4));
        }
        return model;
    }

    
    public CheckBoxTableModel loadCourseModel(){        
        String[] column_names = {"", "Course"};
        Object[][] data = null;
        if(cl.loadCourseList()) {
            String[] courses = cl.getCourseNames();
            Arrays.sort(courses);
            data = new Object[courses.length][2];
            for(int i = 0; i < courses.length; i++) {
                data[i][1] = courses[i];
                data[i][0] = false;
            }
        }  
        
        return new CheckBoxTableModel(column_names, data);
    }
    
    public CheckBoxTableModel loadInterestModel(){
        String[] column_names = {"", "Degree"};
        Object[][] data = null;
        if(cl.loadCourseList()) {
            String[] courses = cl.getCourseNames();
            Arrays.sort(courses);
            ArrayList<String> degrees = new ArrayList<String>();
            for(String course : courses){
                if(!degrees.contains(course.substring(0, 4))){
                    degrees.add(course.substring(0, 4));
                }
            }
            data = new Object[degrees.size()][2];
            for(int i = 0; i < degrees.size(); i++) {
                data[i][1] = degrees.get(i);
                data[i][0] = false;
            }
        }
        return new CheckBoxTableModel(column_names, data);
    }
    
    /**
     * Used to change the column widths and set the table settings
     */
    private void initTableModel() {
        TableColumn col;
        
        takenCourses.getTableHeader().setReorderingAllowed(false);
        col = takenCourses.getColumnModel().getColumn(0);
        col.setMaxWidth(20);
        
        subjects.getTableHeader().setReorderingAllowed(false);
        col = subjects.getColumnModel().getColumn(0);
        col.setMaxWidth(20);
                
        col = takenCourses.getColumnModel().getColumn(1);
        col.setCellRenderer(new ToolTipCellRenderer());
        
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jScrollPane4 = new javax.swing.JScrollPane();
        takenCourses = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        subjects = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "CPSC" }));

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        jLabel1.setText("Degree");

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        jLabel2.setText("Subjects of Interest");

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        jLabel3.setText("Taken Courses");

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        jLabel4.setText("Courses Per Semester");

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(1, 1, 6, 1));
        jSpinner1.setValue(5);

        takenCourses.setModel(loadCourseModel());
        takenCourses.setAutoscrolls(false);
        takenCourses.setIntercellSpacing(new java.awt.Dimension(2, 1));
        takenCourses.getTableHeader().setResizingAllowed(false);
        takenCourses.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(takenCourses);

        subjects.setModel(loadInterestModel());
        subjects.getTableHeader().setResizingAllowed(false);
        subjects.getTableHeader().setReorderingAllowed(false);
        jScrollPane5.setViewportView(subjects);

        jButton1.setBackground(new java.awt.Color(204, 204, 255));
        jButton1.setText("GO!");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        jLabel5.setText("Include Special Topics Courses?");

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("YES");

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("NO");

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        jLabel6.setText("Suggest Electives?");

        buttonGroup2.add(jRadioButton3);
        jRadioButton3.setSelected(true);
        jRadioButton3.setText("YES");

        buttonGroup2.add(jRadioButton4);
        jRadioButton4.setText("NO");

        jButton2.setText("Clear");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearTaken(evt);
            }
        });

        jButton3.setText("Clear");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearSubject(evt);
            }
        });

        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setHorizontalScrollBar(null);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Suggestions"));
        jPanel3.setMaximumSize(new java.awt.Dimension(961, 568));
        jPanel3.setMinimumSize(new java.awt.Dimension(961, 568));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 991, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 545, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(jPanel3);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Scorecard"));
        jPanel2.setLayout(new java.awt.GridLayout(getRuleRows(), 1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jRadioButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton2))
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addGap(4, 4, 4))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3))
                    .addComponent(jLabel2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jRadioButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(12, 12, 12)
                                .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1))
                        .addGap(9, 9, 9)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jRadioButton1)
                            .addComponent(jRadioButton2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jRadioButton3)
                            .addComponent(jRadioButton4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton3)
                                .addGap(0, 0, 0)))
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //RUN AN INFERENCE!!
        
      //get degree
        String degree = jComboBox1.getSelectedItem().toString();
       // jTextArea1.setText("User selected degree: " + degree);
        //get special courses option!
        
        //get number of courses per semester
        int num_courses_a_sem = (int)jSpinner1.getValue();
       // jTextArea1.setText(jTextArea1.getText() + "\r\nUser selected " + num_courses_a_sem + " courses per semester");
        
        //get taken courses by name
       // jTextArea1.setText(jTextArea1.getText() + "\r\n\r\nUser has taken the following courses: ");
        ArrayList<String> taken = new ArrayList<String>();
        for(int i = 0; i < takenCourses.getRowCount(); i++){
            //if selected
            if((Boolean)takenCourses.getValueAt(i, 0)){
           //     jTextArea1.setText(jTextArea1.getText() + "\r\n     " + (String)takenCourses.getValueAt(i,1));
                taken.add((String)takenCourses.getValueAt(i,1));
            }
        }
        //get interests by name
        ArrayList<String> interests = new ArrayList<String>();
        
      //  jTextArea1.setText(jTextArea1.getText() + "\r\n\r\nUser has interest in the following degrees: ");
        
        for(int i = 0; i < subjects.getRowCount(); i++){
            //if selected
            if((Boolean)subjects.getValueAt(i, 0)){
              //  jTextArea1.setText(jTextArea1.getText() + "\r\n     " + (String)subjects.getValueAt(i,1));
               interests.add((String)subjects.getValueAt(i,1));
            }
        }
        if(!interests.contains(degree))
            interests.add(degree);
        //run inference
        session = new Session(num_courses_a_sem);
        session.setInitialCourses(cl.get(taken.toArray(new String[taken.size()])));
        ie = new InferenceEngine(session, cl , rl, interests);
        ie.setInclude_specialized_topics(jRadioButton1.isSelected());
        session = ie.inferDegreeRequirements();
        if(jRadioButton3.isSelected())
            session = ie.inferElectives();
        //show result on jTextArea1
        //Print if rules passed
     //   jTextArea1.setText(jTextArea1.getText() + "\r\n" + "Credit hours: " + session.credit_hours + "/120 = " + (float)session.credit_hours/120 * 100 + "%");
     //   jTextArea1.setText(jTextArea1.getText() + "\r\n" + session.printSemesters());
        //show results of inference
        showSuggestions();
        
        //Update ScoreCard
        setRulesSatisfied();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void showSuggestions(){
        jPanel3.removeAll();
        int num_courses_a_sem = (int)jSpinner1.getValue();      
        jScrollPane1.setPreferredSize(new Dimension(1000,568));
        jPanel3.setLayout(new GridLayout(0, 2));
        int i = 0;
        //Add a panel per semester
        for(Course[] semester_list : session.getSuggestions()){
            i++;
            JPanel sem_panel = new JPanel(new GridLayout(num_courses_a_sem, 0));
            sem_panel.setBorder(new TitledBorder("Semester " + i));
            //Add all courses in course panel
            for(Course c : semester_list){
                JLabel courselbl = new JLabel(c == null ? "No Course" : c.toString());
                sem_panel.add(courselbl);
            }
            jPanel3.add(sem_panel);
        }
    }
    
    private void clearTaken(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearTaken
        for(int i = 0; i < takenCourses.getRowCount(); i++){            
            takenCourses.setValueAt(false, i, 0);
        }
        takenCourses.repaint();
    }//GEN-LAST:event_clearTaken

    private void clearSubject(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearSubject
        for(int i = 0; i < subjects.getRowCount(); i++){            
            subjects.setValueAt(false, i, 0);
        }
        subjects.repaint();
    }//GEN-LAST:event_clearSubject

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUIMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUIMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUIMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUIMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUIMain().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTable subjects;
    private javax.swing.JTable takenCourses;
    // End of variables declaration//GEN-END:variables

    public class CheckBoxTableModel extends AbstractTableModel {
        Object[][] data;
        String[] cols;
        
        CheckBoxTableModel(String[] column_names, Object[][] data){
            this.cols = column_names;
            this.data = data;
        }
         
        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public int getColumnCount() {
            return cols.length;
        }
        
        @Override
        public String getColumnName(int column) {
          return cols[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return data[rowIndex][columnIndex];
        }
        
        @Override
        public void setValueAt(Object value, int row, int column) {
            data[row][column] = value;
        }        
        
        @Override
        public Class getColumnClass(int column){
             return (getValueAt(0, column).getClass());
        }
        
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex){
           return (columnIndex != 1);            
        }
    }
    
    public class ToolTipCellRenderer extends DefaultTableCellRenderer {
    @Override
        public Component getTableCellRendererComponent (JTable table, Object value,
                            boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel c = (JLabel)super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);

            Course course = cl.get(value.toString());
            
            String desc_in = course.getDescription() + " ";
            String desc_out = "";
            int i = 0;
            
            while(i + 80 < desc_in.length()) {                
                desc_out += "<br>" + desc_in.substring(i, desc_in.indexOf(" ", i+80));                
                i += Math.min(desc_in.indexOf(" ", i+80), desc_in.length());
            }  
                        
            c.setToolTipText("<html><b>" + course.getName() + "</b> - " + course.getTitle() 
                    + " (" + course.getCredits() + ")" + desc_out + "</html>");
            return c;
        }
    }
}

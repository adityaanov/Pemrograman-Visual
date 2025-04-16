/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplikasi_koperasi;

import Aplikasi.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adity
 */
public class FormPelanggan extends javax.swing.JFrame {
    private DefaultTableModel model;
    String id_pelanggan, nama_pelanggan, jk, no_hp, alamat;
    
    public FormPelanggan() {
        initComponents();
        Nonaktif();
        
        model = new DefaultTableModel();
        tabel_pelanggan.setModel (model);
        model.addColumn("ID Pelanggan");
        model.addColumn("Nama Pelanggan");
        model.addColumn("Jenis Kelamin");
        model.addColumn("Nomor Handphone");
        model.addColumn("Alamat");
        
        GetData();
        IdOtomatis();
    }
    
    public void GetData(){
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        try{
            Statement stat = (Statement) Koneksi.KoneksiDatabase().createStatement();
            String sql = "Select * From Pelanggan";
            ResultSet rs = stat.executeQuery(sql);
            
            while (rs.next()){
                Object[] obj = new Object[5];
                obj [0] = rs.getString("id_pelanggan");
                obj [1] = rs.getString("nama_panggilan");
                obj [2] = rs.getString("jk");
                obj [3] = rs.getString("no_hp");
                obj [4] = rs.getString("alamat");
                
                model.addRow(obj);
            }
        }catch (SQLException error){
            JOptionPane.showMessageDialog(null, error.getMessage());
        }
    }
    
    public void SelectData(){
        int i = tabel_pelanggan.getSelectedRow();
        if(i == -1){
            return;
        }txt_idpelanggan.setText(""+model.getValueAt(i, 0));
        txt_namapelanggan.setText(""+model.getValueAt(i, 1));
        if("P".equals(model.getValueAt(i, 2).toString())){
            rb_P.setSelected(true);
        }else{
            rb_L.setSelected(true);
        }
        txt_nohp.setText(""+model.getValueAt(i, 3));
        txtarea_alamat.setText(""+model.getValueAt(i, 4));
        btn_edit.setEnabled(true);
        btn_delete.setEnabled(true);
    }
    
    public void IdOtomatis(){
        try{
            com.mysql.jdbc.Statement st = (com.mysql.jdbc.Statement) Koneksi.KoneksiDatabase().createStatement();
            String sql = "Select * From pelanggan";
            
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()){
                String id = rs.getString("Id Pelanggan").substring(3);
                String AN = ""+(Integer.parseInt(id)+1);
                String nol = "";
                
                if(AN.length()==1)
                {nol="00";}
                else if(AN.length()==2)
                {nol="0";}
                else if(AN.length()==3)
                {nol="";}
                    txt_idpelanggan.setText("PN"+nol+AN);
            }else{
                txt_idpelanggan.setText("PN001");
            }
        }catch(SQLException error){
            JOptionPane.showMessageDialog(null, error.getMessage());
        }
    }
    
    public void LoadData(){
        id_pelanggan = txt_idpelanggan.getText();
        nama_pelanggan = txt_namapelanggan.getText();
        jk = null;
        if(rb_L.isSelected()){
            jk = "L";
        }else if (rb_P.isSelected()){
            jk = "P";
        }
        no_hp = txt_nohp.getText();
        alamat = txtarea_alamat.getText();
    }
    
    public void SimpanData(){
        LoadData();
        
        try{
            com.mysql.jdbc.Statement stat = (com.mysql.jdbc.Statement) Koneksi.KoneksiDatabase().createStatement();
            String sql = "Insert Into Pelanggan (id_pelanggan, nama_pelanggan, jk, no_hp, alamat)" + "values" + "('"+id_pelanggan+"', '"+nama_pelanggan+"', '"+jk+"', "+no_hp+"', "+alamat+"')";
            PreparedStatement p = (PreparedStatement) Koneksi.KoneksiDatabase().prepareStatement(sql);
            p.executeUpdate();
            
            GetData();
            Nonaktif();
            
            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
        }catch(SQLException ex){
            Logger.getLogger(FormPelanggan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void UbahData(){
        LoadData();
        
        try{
            com.mysql.jdbc.Statement stat = (com.mysql.jdbc.Statement) Koneksi.KoneksiDatabase().createStatement();
            String sql = "Update pelanggan Set nama_pelanggan = '"+nama_pelanggan+"', "
                    +"jk = '"+jk+"', "
                    +"no_hp = '"+no_hp+"', "
                    +"alamat = '"+alamat+"' WHERE id_pelanggan = '"+id_pelanggan+"'";
            
            PreparedStatement p = (PreparedStatement) Koneksi.KoneksiDatabase().prepareStatement(sql);
            p.executeUpdate();
        
            GetData();
            Kosongkan();
            SelectData();
        
            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
        }catch(SQLException err){
            JOptionPane.showMessageDialog(null, err.getMessage());
        }
    }
    
    public void HapusData(){
        LoadData();
        
        int pesan = JOptionPane.showConfirmDialog(null, "Anda yakin ingin menghapus data pelanggan "+id_pelanggan+"?","konfirmasi",JOptionPane.OK_CANCEL_OPTION);
        if(pesan == JOptionPane.OK_OPTION){
            try{
                com.mysql.jdbc.Statement stat = (com.mysql.jdbc.Statement) Koneksi.KoneksiDatabase().createStatement();
                String sql = "Delete From Pelanggan Where id_pelanggan = "+txt_idpelanggan+"'";
                PreparedStatement p = (PreparedStatement) Koneksi.KoneksiDatabase().prepareStatement(sql);
                p.executeUpdate();
                
                GetData();
                JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus");
            }catch(SQLException error){
                JOptionPane.showConfirmDialog(null, error.getMessage());
            }
        }
    }
    
    public void Nonaktif(){
        txt_idpelanggan.setEnabled(false);
        txt_namapelanggan.setEnabled(false);
        txt_nohp.setEnabled(false);
        txtarea_alamat.setEnabled(false);
        rb_L.setEnabled(false);
        rb_P.setEnabled(false);
        btn_save.setEnabled(false);
        btn_edit.setEnabled(false);
        btn_delete.setEnabled(false);
    }
    
    public void Aktif(){
        txt_idpelanggan.setEnabled(false);
        txt_namapelanggan.setEnabled(true);
        txt_nohp.setEnabled(true);
        txtarea_alamat.setEnabled(true);
        rb_L.setEnabled(true);
        rb_P.setEnabled(true);
        btn_save.setEnabled(true);
        btn_add.setEnabled(false);
        txt_namapelanggan.requestFocus();
    }
    
    public void Kosongkan(){
        txt_namapelanggan.setText("");
        txt_nohp.setText("");
        txtarea_alamat.setText("");
        rb_L.isSelected();
        rb_P.isSelected();
    }
    
    public void Cari(){
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        try{
            Connection con = Koneksi.KoneksiDatabase();
            Statement st = con.createStatement();
            String sql = "Select * From Pelanggan Where nama_pelanggan like '%"+txt_cari.getText()+"&'";
            ResultSet rs = st.executeQuery(sql);
                    
            while (rs.next()){
                Object[] ob = new Object[5];
                ob [0] = rs.getString("id_pelanggan");
                ob [1] = rs.getString("nama_pelanggan");
                ob [2] = rs.getString("jk");
                ob [3] = rs.getString("no_hp");
                ob [4] = rs.getString("alamat");
                        
                model.addRow(ob);
            }
        }catch(SQLException e){
            System.out.println(e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txt_idpelanggan = new javax.swing.JTextField();
        txt_namapelanggan = new javax.swing.JTextField();
        rb_L = new javax.swing.JRadioButton();
        rb_P = new javax.swing.JRadioButton();
        txt_nohp = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtarea_alamat = new javax.swing.JTextArea();
        txt_cari = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabel_pelanggan = new javax.swing.JTable();
        btn_save = new javax.swing.JButton();
        btn_edit = new javax.swing.JButton();
        btn_delete = new javax.swing.JButton();
        btn_add = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();

        jButton1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jButton1.setText("Tambah");
        jButton1.setPreferredSize(new java.awt.Dimension(601, 521));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1138, 598));

        jPanel3.setBackground(new java.awt.Color(230, 0, 19));
        jPanel3.setPreferredSize(new java.awt.Dimension(1150, 60));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("DATA PELANGGAN");
        jLabel1.setToolTipText("");
        jLabel1.setAlignmentX(0.5F);
        jLabel1.setAutoscrolls(true);
        jLabel1.setPreferredSize(new java.awt.Dimension(340, 43));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(410, 410, 410)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(400, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(230, 0, 19));
        jPanel2.setPreferredSize(new java.awt.Dimension(1150, 10));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1150, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanel4.setBackground(new java.awt.Color(230, 0, 19));
        jPanel4.setPreferredSize(new java.awt.Dimension(1150, 10));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1150, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanel5.setBackground(new java.awt.Color(230, 0, 19));
        jPanel5.setPreferredSize(new java.awt.Dimension(1150, 510));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setPreferredSize(new java.awt.Dimension(1130, 484));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setText("ID Pelanggan");

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel3.setText("Nama Pelanggan");

        jLabel4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel4.setText("Jenis Kelamin");

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel5.setText("Nomor Handphone");

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel6.setText("Alamat");

        txt_idpelanggan.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txt_idpelanggan.setPreferredSize(new java.awt.Dimension(300, 30));

        txt_namapelanggan.setPreferredSize(new java.awt.Dimension(300, 30));
        txt_namapelanggan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_namapelangganKeyTyped(evt);
            }
        });

        rb_L.setBackground(new java.awt.Color(255, 255, 255));
        rb_L.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        rb_L.setText("Laki-Laki");
        rb_L.setPreferredSize(new java.awt.Dimension(100, 25));

        rb_P.setBackground(new java.awt.Color(255, 255, 255));
        rb_P.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        rb_P.setText("Perempuan");
        rb_P.setPreferredSize(new java.awt.Dimension(100, 25));

        txt_nohp.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txt_nohp.setPreferredSize(new java.awt.Dimension(300, 30));
        txt_nohp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_nohpKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_nohpKeyTyped(evt);
            }
        });

        jScrollPane1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jScrollPane1.setPreferredSize(new java.awt.Dimension(300, 75));

        txtarea_alamat.setColumns(20);
        txtarea_alamat.setRows(5);
        jScrollPane1.setViewportView(txtarea_alamat);

        txt_cari.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txt_cari.setPreferredSize(new java.awt.Dimension(650, 30));
        txt_cari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_cariKeyTyped(evt);
            }
        });

        jScrollPane2.setPreferredSize(new java.awt.Dimension(700, 300));

        tabel_pelanggan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabel_pelanggan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabel_pelangganMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabel_pelanggan);

        btn_save.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btn_save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/save.png"))); // NOI18N
        btn_save.setText("Simpan");
        btn_save.setToolTipText("");
        btn_save.setPreferredSize(new java.awt.Dimension(125, 40));
        btn_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveActionPerformed(evt);
            }
        });

        btn_edit.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btn_edit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/edit.png"))); // NOI18N
        btn_edit.setText("Edit");
        btn_edit.setPreferredSize(new java.awt.Dimension(125, 40));
        btn_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editActionPerformed(evt);
            }
        });

        btn_delete.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btn_delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/delete.png"))); // NOI18N
        btn_delete.setText("Hapus");
        btn_delete.setPreferredSize(new java.awt.Dimension(125, 41));
        btn_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteActionPerformed(evt);
            }
        });

        btn_add.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btn_add.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/add.png"))); // NOI18N
        btn_add.setText("Tambah");
        btn_add.setPreferredSize(new java.awt.Dimension(125, 40));
        btn_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addActionPerformed(evt);
            }
        });

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/search.png"))); // NOI18N
        jLabel7.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_idpelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_nohp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_namapelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(rb_L, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(rb_P, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel5))
                .addGap(60, 60, 60)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(32, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_add, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_save, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_edit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_delete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(122, 122, 122))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(txt_idpelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_namapelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rb_L, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rb_P, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_nohp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_save, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_edit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_delete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_add, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(235, 235, 235))
        );

        btn_add.getAccessibleContext().setAccessibleName("jButton1");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 1124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 483, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1150, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addActionPerformed
        IdOtomatis();
        Kosongkan();
        Aktif();
        txt_namapelanggan.requestFocus();
    }//GEN-LAST:event_btn_addActionPerformed

    private void btn_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveActionPerformed
        SimpanData();
        Kosongkan();
        IdOtomatis();
        Nonaktif();
        btn_add.setEnabled(true);
        btn_save.setEnabled(false);
        btn_edit.setEnabled(false);
        btn_delete.setEnabled(false);
    }//GEN-LAST:event_btn_saveActionPerformed

    private void btn_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editActionPerformed
        UbahData();
        Kosongkan();
        Nonaktif();
        btn_add.setEnabled(true);
        btn_save.setEnabled(false);
        btn_edit.setEnabled(false);
        btn_delete.setEnabled(false);
    }//GEN-LAST:event_btn_editActionPerformed

    private void btn_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteActionPerformed
        HapusData();
        Kosongkan();
        IdOtomatis();
        Nonaktif();
        btn_add.setEnabled(true);
        btn_save.setEnabled(false);
        btn_edit.setEnabled(false);
        btn_delete.setEnabled(false);
    }//GEN-LAST:event_btn_deleteActionPerformed

    private void tabel_pelangganMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabel_pelangganMouseClicked
        SelectData();
        Aktif();
        txt_idpelanggan.setEnabled(false);
        btn_save.setEnabled(false);
    }//GEN-LAST:event_tabel_pelangganMouseClicked

    private void txt_namapelangganKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_namapelangganKeyTyped
        if(Character.isDigit(evt.getKeyChar())){
            evt.consume();
            JOptionPane.showMessageDialog(null, "Pada kolom nama hanya bisa memasukkan karakter huruf");
        }
    }//GEN-LAST:event_txt_namapelangganKeyTyped

    private void txt_nohpKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nohpKeyTyped
        if(Character.isAlphabetic(evt.getKeyChar())){
            evt.consume();
            JOptionPane.showMessageDialog(null, "Pada kolom nomor handphone hanya bisa memasukkan karakter angka");
        }
    }//GEN-LAST:event_txt_nohpKeyTyped

    private void txt_nohpKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nohpKeyReleased
        String input = txt_nohp.getText();
        if(input.length()>13){
            JOptionPane.showMessageDialog(rootPane, "Jumlah input melebihi batas");
            txt_nohp.setText("");
        }
    }//GEN-LAST:event_txt_nohpKeyReleased

    private void txt_cariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_cariKeyTyped
        Cari();
    }//GEN-LAST:event_txt_cariKeyTyped

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
            java.util.logging.Logger.getLogger(FormPelanggan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormPelanggan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormPelanggan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormPelanggan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormPelanggan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_add;
    private javax.swing.JButton btn_delete;
    private javax.swing.JButton btn_edit;
    private javax.swing.JButton btn_save;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JRadioButton rb_L;
    private javax.swing.JRadioButton rb_P;
    private javax.swing.JTable tabel_pelanggan;
    private javax.swing.JTextField txt_cari;
    private javax.swing.JTextField txt_idpelanggan;
    private javax.swing.JTextField txt_namapelanggan;
    private javax.swing.JTextField txt_nohp;
    private javax.swing.JTextArea txtarea_alamat;
    // End of variables declaration//GEN-END:variables
}

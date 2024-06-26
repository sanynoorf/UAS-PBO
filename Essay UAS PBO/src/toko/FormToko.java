package toko;

import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FormToko extends JFrame {
    private String[] judul = {"Kode Barang", "Nama Barang", "Harga Barang", "Stok Barang"};
    DefaultTableModel df;
    JTable tab = new JTable();
    JScrollPane scp = new JScrollPane();
    JPanel pnl = new JPanel();
    JLabel lblkode_barang = new JLabel("Kode Barang");
    JTextField txkode_barang = new JTextField(20);
    JLabel lblnama_barang = new JLabel("Nama Barang");
    JTextField txnama_barang = new JTextField(10);
    JLabel lblharga_barang = new JLabel("Harga Barang");
    JTextField txharga_barang = new JTextField(20);
    JLabel lblstok_barang = new JLabel("Stok Barang");
    JTextField txstok_barang = new JTextField(10);
    JButton btadd = new JButton("Simpan");
    JButton btnew = new JButton("Baru");
    JButton btdel = new JButton("Hapus");
    JButton btedit = new JButton("Ubah");

    FormToko() {
        super("Data Toko");
        setSize(460, 300);
        pnl.setLayout(null);
        pnl.add(lblkode_barang);
        lblkode_barang.setBounds(20, 10, 80, 20);
        pnl.add(txkode_barang);
        txkode_barang.setBounds(105, 10, 100, 20);
        pnl.add(lblnama_barang);
        lblnama_barang.setBounds(20, 33, 80, 20);
        pnl.add(txnama_barang);
        txnama_barang.setBounds(105, 33, 175, 20);
        pnl.add(lblharga_barang);
        lblharga_barang.setBounds(20, 56, 80, 20);
        pnl.add(txharga_barang);
        txharga_barang.setBounds(105, 56, 175, 20);
        pnl.add(lblstok_barang);
        lblstok_barang.setBounds(20, 79, 80, 20);
        pnl.add(txstok_barang);
        txstok_barang.setBounds(105, 79, 175, 20);

        pnl.add(btnew);
        btnew.setBounds(300, 10, 125, 20);
        btnew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnewAksi(e);
            }
        });
        pnl.add(btadd);
        btadd.setBounds(300, 33, 125, 20);
        btadd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btaddAksi(e);
            }
        });
        pnl.add(btedit);
        btedit.setBounds(300, 56, 125, 20);
        btedit.setEnabled(false);
        btedit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bteditAksi(e);
            }
        });
        pnl.add(btdel);
        btdel.setBounds(300, 79, 125, 20);
        btdel.setEnabled(false);
        btdel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btdelAksi(e);
            }
        });
        df = new DefaultTableModel(null, judul);
        tab.setModel(df);
        scp.getViewport().add(tab);
        tab.setEnabled(true);
        tab.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                tabMouseClicked(evt);
            }
        });
        scp.setBounds(20, 110, 405, 130);
        pnl.add(scp);
        getContentPane().add(pnl);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    void loadData() {
        try {
            Connection cn = new connecDB().getConnect();
            Statement st = cn.createStatement();
            String sql = "SELECT * FROM tbl_barang";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String kode_barang = rs.getString("kode_barang");
                String nama_barang = rs.getString("nama_barang");
                String harga_barang = rs.getString("harga_barang");
                String stok_barang = rs.getString("stok_barang");
                String[] data = {kode_barang, nama_barang, harga_barang, stok_barang};
                df.addRow(data);
            }
            rs.close();
            cn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }    

    void clearTable() {
        int numRow = df.getRowCount();
        for (int i = 0; i < numRow; i++) {
            df.removeRow(0);
        }
    }

    void clearTextField() {
        txkode_barang.setText(null);
        txnama_barang.setText(null);
        txharga_barang.setText(null);
        txstok_barang.setText(null);
    }

    void simpanData(Toko M) {
        String sql = "INSERT INTO tbl_barang (kode_barang, nama_barang, harga_barang, stok_barang) VALUES (?, ?, ?, ?)";
        try (Connection cn = new connecDB().getConnect();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, M.getKode_barang());
            ps.setString(2, M.getNama_barang());
            ps.setInt(3, M.getHarga_barang());
            ps.setInt(4, M.getStok_barang());
            int result = ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan", "Info Proses", JOptionPane.INFORMATION_MESSAGE);
            String[] data = {M.getKode_barang(), M.getNama_barang(), String.valueOf(M.getHarga_barang()), String.valueOf(M.getStok_barang())};
            df.addRow(data);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void hapusData(String kode_barang) {
        String sql = "DELETE FROM tbl_barang WHERE kode_barang = ?";
        try (Connection cn = new connecDB().getConnect();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, kode_barang);
            int result = ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus", "Info Proses", JOptionPane.INFORMATION_MESSAGE);
            df.removeRow(tab.getSelectedRow());
            clearTextField();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void ubahData(Toko M, String kode_barang) {
        String sql = "UPDATE tbl_barang SET kode_barang=?, nama_barang=?, harga_barang=?, stok_barang=? WHERE kode_barang=?";
        try (Connection cn = new connecDB().getConnect();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, M.getKode_barang());
            ps.setString(2, M.getNama_barang());
            ps.setInt(3, M.getHarga_barang());
            ps.setInt(4, M.getStok_barang());
            ps.setString(5, kode_barang);
            int result = ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah", "Info Proses", JOptionPane.INFORMATION_MESSAGE);
            clearTable();
            loadData();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void btnewAksi(ActionEvent evt) {
        clearTextField();
        btedit.setEnabled(false);
        btdel.setEnabled(false);
        btadd.setEnabled(true);
        txkode_barang.setEnabled(true);
        txkode_barang.requestFocus();
    }

    private void btaddAksi(ActionEvent evt) {
        Toko M = new Toko();
        M.setKode_barang(txkode_barang.getText());
        M.setNama_barang(txnama_barang.getText());
        M.setHarga_barang(Integer.parseInt(txharga_barang.getText()));
        M.setStok_barang(Integer.parseInt(txstok_barang.getText()));
        simpanData(M);
        clearTextField();
        btedit.setEnabled(false);
        btdel.setEnabled(false);
        btadd.setEnabled(true);
    }

    private void btdelAksi(ActionEvent evt) {
        hapusData(txkode_barang.getText());
        btedit.setEnabled(false);
        btdel.setEnabled(false);
        btadd.setEnabled(true);
        clearTextField();
    }

    private void bteditAksi(ActionEvent evt) {
        Toko M = new Toko();
        M.setKode_barang(txkode_barang.getText());
        M.setNama_barang(txnama_barang.getText());
        M.setHarga_barang(Integer.parseInt(txharga_barang.getText()));
        M.setStok_barang(Integer.parseInt(txstok_barang.getText()));
        ubahData(M, txkode_barang.getText());
        clearTextField();
        btedit.setEnabled(false);
        btdel.setEnabled(false);
        btadd.setEnabled(true);
    }

    private void tabMouseClicked(MouseEvent evt) {
        int row = tab.getSelectedRow();
        txkode_barang.setText(tab.getValueAt(row, 0).toString());
        txnama_barang.setText(tab.getValueAt(row, 1).toString());
        txharga_barang.setText(tab.getValueAt(row, 2).toString());
        txstok_barang.setText(tab.getValueAt(row, 3).toString());
        txkode_barang.setEnabled(false);
        btedit.setEnabled(true);
        btdel.setEnabled(true);
        btadd.setEnabled(false);
    }

    public static void main(String[] args) {
        new FormToko().loadData();
    }
}
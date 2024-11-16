package RankingTable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RankingTable {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RankingTable::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Ranking Table with Image Button");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 테이블 모델 정의
        String[] columnNames = {"Ranking", "ID", "Tier", "Score", "Attempts", "Successes"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // 데이터 읽기 및 추가
        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            String line;
            int ranking = 1;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String id = parts[0].trim();
                int attempts = Integer.parseInt(parts[1].trim());
                int successes = Integer.parseInt(parts[2].trim());

                // 점수 계산 및 티어 결정
                int score = calculateScore(successes, attempts);
                String tier = determineTier(score);

                // 테이블에 추가
                model.addRow(new Object[]{ranking++, id, tier, score, attempts, successes});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // JTable 생성
        JTable table = new JTable(model);
        table.getColumn("Tier").setCellRenderer(new TierColor());
        JScrollPane tableScrollPane = new JScrollPane(table);

        // 버튼 및 이미지 패널 생성
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton showImageButton = new JButton("점수 산정에 관한 도움말");
        JLabel imageLabel = new JLabel();

        // 초기에는 이미지 숨기기
        imageLabel.setVisible(false);

        // 버튼 클릭 이벤트 처리
        showImageButton.addActionListener(new ActionListener() {
            private boolean isImageVisible = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                isImageVisible = !isImageVisible; // 상태를 토글
                imageLabel.setVisible(isImageVisible);
                if (isImageVisible) {
                    showImageButton.setText("도움말 닫기");
                } else {
                    showImageButton.setText("점수 산정에 관한 도움말");
                }
            }
        });

        // 이미지 경로 설정 및 추가
        imageLabel.setIcon(new ImageIcon(RankingTable.class.getResource("/help image.png")));
        bottomPanel.add(showImageButton);
        bottomPanel.add(imageLabel);

        // 메인 레이아웃
        frame.setLayout(new BorderLayout());
        frame.add(tableScrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // 창 크기 설정
        frame.setSize(1000, 800);
        frame.setVisible(true);
    }

    private static int calculateScore(int successes, int attempts) {
        return attempts == 0 ? 0 : (int) ((double) successes / attempts * 100);
    }

    private static String determineTier(int score) {
        if (score >= 95) {
            return "Diamond";
        } else if (score >= 85) {
            return "Platinum";
        } else if (score >= 75) {
            return "Gold";
        } else if (score >= 60) {
            return "Silver";
        } else {
            return "Bronze";
        }
    }

    static class TierColor extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (value != null) {
                String tier = value.toString();
                switch (tier) {
                    case "Diamond":
                        cell.setBackground(new Color(185, 242, 255));
                        break;
                    case "Platinum":
                        cell.setBackground(Color.GREEN);
                        break;
                    case "Gold":
                        cell.setBackground(Color.YELLOW);
                        break;
                    case "Silver":
                        cell.setBackground(Color.LIGHT_GRAY);
                        break;
                    case "Bronze":
                        cell.setBackground(new Color(205, 127, 50));
                        break;
                    default:
                        cell.setBackground(Color.WHITE);
                        break;
                }
            }

            return cell;
        }
    }
}

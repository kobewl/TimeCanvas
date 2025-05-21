package com.time.canvas.dao;

import com.time.canvas.domain.AiConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@Slf4j
public class AiConfigDao {

    private final DataSource dataSource;

    @Autowired
    public AiConfigDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public AiConfig getConfig(String modelName) {
        String sql = "SELECT base_url, api_key, model_name FROM ai_config WHERE model_name = ? ORDER BY id DESC LIMIT 1";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, modelName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    AiConfig config = new AiConfig();
                    config.setBaseUrl(rs.getString("base_url"));
                    config.setApiKey(rs.getString("api_key"));
                    config.setModelName(rs.getString("model_name"));
                    return config;
                }
            }
        } catch (SQLException e) {
            log.error("Error fetching AiConfig from database for model {}: {}", modelName, e.getMessage(), e);
        }
        return null;
    }
} 
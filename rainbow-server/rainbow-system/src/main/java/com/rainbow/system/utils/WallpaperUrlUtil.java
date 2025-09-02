package com.rainbow.system.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class WallpaperUrlUtil {

    private static final String BING_BASE_URL = "https://www.bing.com";
    private static final String DEFAULT_RESOLUTION = "1920x1080";

    /**
     * 构建完整的图片URL
     */
    public String buildFullImageUrl(String relativeUrl) {
        return buildFullImageUrl(relativeUrl, DEFAULT_RESOLUTION);
    }

    /**
     * 构建指定分辨率的图片URL
     */
    public String buildFullImageUrl(String relativeUrl, String resolution) {
        if (!StringUtils.hasText(relativeUrl)) {
            return null;
        }

        if (relativeUrl.startsWith("http")) {
            return relativeUrl;
        }

        String url = BING_BASE_URL + relativeUrl;
        
        // 如果URL中没有分辨率参数，则添加默认分辨率
        if (!url.contains("_") && StringUtils.hasText(resolution)) {
            url = url.replace(".jpg", "_" + resolution + ".jpg");
        }
        
        return url;
    }

    /**
     * 构建缩略图URL
     */
    public String buildThumbnailUrl(String relativeUrl) {
        return buildFullImageUrl(relativeUrl, "480x270");
    }

    /**
     * 从URL中提取文件名
     */
    public String extractFileName(String url) {
        if (!StringUtils.hasText(url)) {
            return null;
        }

        // 移除查询参数
        int queryIndex = url.indexOf('?');
        if (queryIndex > 0) {
            url = url.substring(0, queryIndex);
        }

        // 提取文件名
        int lastSlashIndex = url.lastIndexOf('/');
        if (lastSlashIndex >= 0) {
            return url.substring(lastSlashIndex + 1);
        }

        return url;
    }

    /**
     * 检查URL是否为有效的图片URL
     */
    public boolean isValidImageUrl(String url) {
        if (!StringUtils.hasText(url)) {
            return false;
        }

        String lowerUrl = url.toLowerCase();
        return lowerUrl.contains(".jpg") || lowerUrl.contains(".jpeg") || 
               lowerUrl.contains(".png") || lowerUrl.contains(".webp");
    }

    /**
     * 获取不同尺寸的图片URL
     */
    public String getImageUrlBySize(String baseUrl, String size) {
        if (!StringUtils.hasText(baseUrl)) {
            return null;
        }

        // 如果已经包含尺寸，先移除
        String cleanUrl = baseUrl.replaceAll("_\\d+x\\d+", "");
        
        // 添加新的尺寸
        return cleanUrl.replace(".jpg", "_" + size + ".jpg");
    }

    /**
     * 构建Bing搜索URL
     */
    public String buildSearchUrl(String query) {
        if (!StringUtils.hasText(query)) {
            return null;
        }
        return "https://www.bing.com/search?q=" + query.replace(" ", "+");
    }
} 
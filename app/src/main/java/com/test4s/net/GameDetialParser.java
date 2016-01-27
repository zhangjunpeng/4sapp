package com.test4s.net;

import com.test4s.gdb.GameInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/1/20.
 */
public class GameDetialParser {
    boolean success;
    int code;
    String msg;
    GameInfo gameInfo;
    String create_time;
    String game_test_nums;
    String game_intro;
    List<String> game_shot;
    String game_update_intro;
    int is_care;
    String staticUrl;
    String webUrl;
    float score;
    CpInfo cpInfo;
    List<GameInfo> cpGame;

    public List<String> getGame_shot() {
        return game_shot;
    }

    public void setGame_shot(List<String> game_shot) {
        this.game_shot = game_shot;
    }

    public List<GameInfo> getCpGame() {
        return cpGame;
    }

    public void setCpGame(List<GameInfo> cpGame) {
        this.cpGame = cpGame;
    }

    public CpInfo getCpInfo() {
        return cpInfo;
    }

    public void setCpInfo(CpInfo cpInfo) {
        this.cpInfo = cpInfo;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public GameInfo getGameInfo() {
        return gameInfo;
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getGame_test_nums() {
        return game_test_nums;
    }

    public void setGame_test_nums(String game_test_nums) {
        this.game_test_nums = game_test_nums;
    }

    public String getGame_intro() {
        return game_intro;
    }

    public void setGame_intro(String game_intro) {
        this.game_intro = game_intro;
    }



    public String getGame_update_intro() {
        return game_update_intro;
    }

    public void setGame_update_intro(String game_update_intro) {
        this.game_update_intro = game_update_intro;
    }

    public int getIs_care() {
        return is_care;
    }

    public void setIs_care(int is_care) {
        this.is_care = is_care;
    }

    public String getStaticUrl() {
        return staticUrl;
    }

    public void setStaticUrl(String staticUrl) {
        this.staticUrl = staticUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public class CpInfo{
        String city_id;
        String company_scale;
        String compay_phone;
        String company_site;
        String require;

        public String getCity_id() {
            return city_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }

        public String getCompany_scale() {
            return company_scale;
        }

        public void setCompany_scale(String company_scale) {
            this.company_scale = company_scale;
        }

        public String getCompay_phone() {
            return compay_phone;
        }

        public void setCompay_phone(String compay_phone) {
            this.compay_phone = compay_phone;
        }

        public String getCompany_site() {
            return company_site;
        }

        public void setCompany_site(String company_site) {
            this.company_site = company_site;
        }

        public String getRequire() {
            return require;
        }

        public void setRequire(String require) {
            this.require = require;
        }
    }
}

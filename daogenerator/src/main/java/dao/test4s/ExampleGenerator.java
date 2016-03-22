package dao.test4s;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class ExampleGenerator {
    public static void main(String[] args) throws Exception{
        Schema schema = new Schema(1, "com.test4s.gdb");
        addGameInfo(schema);
        addInformation(schema);
        new DaoGenerator().generateAll(schema, "E:\\work\\MyApp\\app\\src\\main\\java");
    }


    private static void addGameInfo(Schema schema) {
        Entity gameInfo = schema.addEntity("GameInfo");
        Entity investment=schema.addEntity("Investment");
        Entity outSource=schema.addEntity("OutSource");
        Entity ip=schema.addEntity("IP");
        Entity cp=schema.addEntity("CP");
        Entity distribution=schema.addEntity("Distribution");

        //游戏详情
        gameInfo.addIdProperty().primaryKey();
        gameInfo.addStringProperty("sort");
        gameInfo.addStringProperty("game_id");
        gameInfo.addStringProperty("require");
        gameInfo.addStringProperty("game_img");
        gameInfo.addStringProperty("game_download_nums");
        gameInfo.addStringProperty("game_platform");
        gameInfo.addStringProperty("game_stage");
        gameInfo.addStringProperty("game_name");
        gameInfo.addStringProperty("game_download_url");
        gameInfo.addStringProperty("game_size");
        gameInfo.addStringProperty("norms");
        gameInfo.addStringProperty("game_grade");
        gameInfo.addStringProperty("game_type");

        //Investment投资公司
        investment.addIdProperty().primaryKey();
        investment.addStringProperty("company_name");
        investment.addStringProperty("identity_cat");
        investment.addStringProperty("logo");
        investment.addStringProperty("user_id");

        investment.addStringProperty("introuduction");
        investment.addStringProperty("location");
        investment.addStringProperty("scale");
        investment.addStringProperty("webSite");
        investment.addStringProperty("telePhone");
        investment.addStringProperty("address");




        //外包公司
        outSource.addIdProperty().primaryKey();

        //index参数
        outSource.addStringProperty("company_name");
        outSource.addStringProperty("identity_cat");
        outSource.addStringProperty("logo");
        outSource.addStringProperty("user_id");

        outSource.addStringProperty("type");
        outSource.addStringProperty("introuduction");
        outSource.addStringProperty("location");
        outSource.addStringProperty("scale");
        outSource.addStringProperty("webSite");
        outSource.addStringProperty("telePhone");
        outSource.addStringProperty("address");



        //ip

        //index
        ip.addStringProperty("company_name");
        ip.addStringProperty("id");
        ip.addStringProperty("ip_name");
        ip.addStringProperty("ip_logo");
        ip.addStringProperty("ip_style");
        ip.addStringProperty("ip_cat");
        ip.addStringProperty("uthority");

        ip.addStringProperty("type");
        ip.addStringProperty("style");
        ip.addStringProperty("range");
        ip.addStringProperty("introuduction");
        ip.addStringProperty("location");
        ip.addStringProperty("scale");
        ip.addStringProperty("webSite");
        ip.addStringProperty("telePhone");
        ip.addStringProperty("address");
        ip.addStringProperty("otherIp");

        //cp游戏开发
        cp.addIdProperty().primaryKey();
        cp.addStringProperty("company_name");
        cp.addStringProperty("identity_cat");
        cp.addStringProperty("logo");
        cp.addStringProperty("user_id");
        //index
        cp.addStringProperty("introuduction");
        cp.addStringProperty("location");
        cp.addStringProperty("scale");
        cp.addStringProperty("webSite");
        cp.addStringProperty("telePhone");
        cp.addStringProperty("address");


        //distribution
        distribution.addIdProperty().primaryKey();
        distribution.addStringProperty("name");
        distribution.addStringProperty("introuduction");
        distribution.addStringProperty("location");
        distribution.addStringProperty("scale");
        distribution.addStringProperty("webSite");
        distribution.addStringProperty("telePhone");
        distribution.addStringProperty("address");



    }

    private static void addInformation(Schema schema) {
        Entity information = schema.addEntity("Information");

        information.addIdProperty().primaryKey();
        information.addStringProperty("title").notNull();
        information.addStringProperty("time").notNull();
        information.addIntProperty("looknum");
        information.addIntProperty("comment_num");
        information.addStringProperty("context");


    }
}

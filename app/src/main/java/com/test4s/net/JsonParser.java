package com.test4s.net;

import com.test4s.gdb.CP;
import com.test4s.gdb.GameInfo;
import com.test4s.gdb.IP;
import com.test4s.gdb.Investment;
import com.test4s.gdb.OutSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/13.
 */
public class JsonParser {
    public static List<CP> cpList;
    public static List<Investment> investmentList;
    public static List<OutSource> outSourceList;
    public static List<IP> ipList;

    static String[] name_list={"hotGameList","loveGameList","localGameList"};

    public static IndexParser getIndexParser(String data){

        IndexParser indexParser=new IndexParser();

        try {
            cpList=new ArrayList<>();


            JSONObject jsonObject=new JSONObject(data);
            JSONObject jsonObject1=jsonObject.getJSONObject("data");
            JSONArray jsonArray=jsonObject1.getJSONArray("cpList");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject2=jsonArray.getJSONObject(i);
                CP cp=new CP();
                cp.setUser_id(jsonObject2.getString("user_id"));
                cp.setIdentity_cat(jsonObject2.getString("identity_cat"));
                cp.setCompany_name(jsonObject2.getString("company_name"));
                cp.setLogo(jsonObject2.getString("logo"));
                cpList.add(cp);
            }

            JSONArray jsonArray1=jsonObject1.getJSONArray("investorList");
            investmentList=new ArrayList<>();
            for (int i=0;i<jsonArray1.length();i++){
                JSONObject jsonObject3=jsonArray1.getJSONObject(i);
                Investment investment=new Investment();
                investment.setUser_id(jsonObject3.getString("user_id"));
                investment.setIdentity_cat(jsonObject3.getString("identity_cat"));
                investment.setCompany_name(jsonObject3.getString("company_name"));
                investment.setLogo(jsonObject3.getString("logo"));
                investmentList.add(investment);
            }

            JSONArray jsonArray2=jsonObject1.getJSONArray("outsourceList");
            outSourceList=new ArrayList<>();
            for (int i=0;i<jsonArray2.length();i++){
                JSONObject jsonObject3=jsonArray2.getJSONObject(i);
                OutSource outSource=new OutSource();
                outSource.setUser_id(jsonObject3.getString("user_id"));
                outSource.setIdentity_cat(jsonObject3.getString("identity_cat"));
                outSource.setCompany_name(jsonObject3.getString("company_name"));
                outSource.setLogo(jsonObject3.getString("logo"));
                outSourceList.add(outSource);
            }

            JSONArray jsonArray3=jsonObject1.getJSONArray("ipList");
            ipList=new ArrayList<>();
            for (int i=0;i<jsonArray3.length();i++){
                JSONObject jsonObject3=jsonArray3.getJSONObject(i);
                IP ip=new IP();
                ip.setId(jsonObject3.getString("id"));
                ip.setCompany_name(jsonObject3.getString("company_name"));
                ip.setIp_name(jsonObject3.getString("ip_name"));
                ip.setIp_logo(jsonObject3.getString("ip_logo"));
                ip.setIp_style(jsonObject3.getString("ip_style"));
                ip.setIp_cat(jsonObject3.getString("ip_cat"));
                ip.setUthority(jsonObject3.getString("uthority"));
                ipList.add(ip);
            }
            indexParser.setCode(jsonObject.getInt("code"));
            indexParser.setMsg(jsonObject.getString("msg"));
            indexParser.setSuccess(jsonObject.getBoolean("success"));


            IndexParser.data da=indexParser.new data();
            da.setCpList(cpList);
            da.setInvestorList(investmentList);
            da.setIpList(ipList);
            da.setOutSourceList(outSourceList);
            da.setPrefixPic(jsonObject1.getString("prefixPic"));

            indexParser.setData(da);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return indexParser;
    }

    public static GameIndexParser getGameInexParser(String result){
        GameIndexParser gameIndexParser=null;

        try {
            JSONObject jsonObject=new JSONObject(result);
            boolean success=jsonObject.getBoolean("success");
            if (success){
                gameIndexParser=new GameIndexParser();
                gameIndexParser.setSuccess(success);
                gameIndexParser.setCode(jsonObject.getInt("code"));
                JSONObject jsonObject1=jsonObject.getJSONObject("data");

                gameIndexParser.setPrefixPic(jsonObject1.getString("prefixPic"));
                //
                JSONArray advert=jsonObject1.getJSONArray("adverts");
                List<Adverts> advertsList=new ArrayList<>();
                for (int i=0;i<advert.length();i++){
                    Adverts adverts=new Adverts();
                    JSONObject adv=advert.getJSONObject(i);
                    adverts.setAdvert_name(adv.getString("advert_name"));
                    adverts.setAdvert_pic(adv.getString("advert_pic"));
                    adverts.setAdvert_url(adv.getString("advert_url"));
                    advertsList.add(adverts);
                }
                gameIndexParser.setAdvertsList(advertsList);
                //
                for (int i=0;i<name_list.length;i++){
                    JSONArray array=jsonObject1.getJSONArray(name_list[i]);
                    List<GameInfo> gameInfos=new ArrayList<>();
                    for (int j=0;j<array.length();j++){
                        GameInfo gameInfo=new GameInfo();
                        JSONObject game=array.getJSONObject(j);
                        gameInfo.setSort(game.getString("sort"));
                        gameInfo.setGame_id(game.getString("game_id"));
                        gameInfo.setRequire(game.getString("require"));
                        gameInfo.setGame_img(game.getString("game_img"));
                        gameInfo.setGame_download_nums(game.getString("game_download_nums"));
                        gameInfo.setGame_platform(game.getString("game_platform"));
                        gameInfo.setGame_stage(game.getString("game_stage"));
                        gameInfo.setGame_name(game.getString("game_name"));
                        gameInfos.add(gameInfo);
                    }
                   switch (i){
                       case 0:
                           gameIndexParser.setHotGameList(gameInfos);
                           break;
                       case 1:
                           gameIndexParser.setLoveGameList(gameInfos);
                           break;
                       case 2:
                           gameIndexParser.setLocalGameList(gameInfos);
                           break;
                   }
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return gameIndexParser;
    }
    public static GameListParser getGameListParser(String result){
        GameListParser gameListParser=null;
        try {
            JSONObject jsonObject=new JSONObject(result);
            int code=jsonObject.getInt("code");
            if (code==200){
                gameListParser=new GameListParser();
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                gameListParser.setP(jsonObject1.getString("p"));
                gameListParser.setStaticUrl(jsonObject1.getString("staticUrl"));
                gameListParser.setWebUrl(jsonObject1.getString("webUrl"));

                JSONArray jsonArray=jsonObject1.getJSONArray("gameList");
                List<GameInfo> gameInfos=new ArrayList<>();
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject game=jsonArray.getJSONObject(i);
                    GameInfo gameInfo=new GameInfo();
                    gameInfo.setGame_name(game.getString("game_name"));
                    gameInfo.setGame_id(game.getString("game_id"));
                    gameInfo.setGame_img(game.getString("game_img"));
                    gameInfo.setGame_download_url(game.getString("game_download_url"));
                    gameInfo.setGame_download_nums(game.getString("game_download_nums"));
                    gameInfo.setRequire(game.getString("require"));
                    gameInfo.setGame_size(game.getString("game_size"));

                    gameInfos.add(gameInfo);
                }
                gameListParser.setGameInfoList(gameInfos);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return gameListParser;
    }
    public static GameDetialParser getGameDetialParser(String result){
        GameDetialParser gameDetialParser=null;
        try {
            JSONObject jsonObject=new JSONObject(result);
            int code=jsonObject.getInt("code");
            if (code==200){
                gameDetialParser=new GameDetialParser();
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                JSONObject jsonObject2=jsonObject1.getJSONObject("gameInfo");
                GameInfo gameInfo=new GameInfo();
                gameInfo.setGame_name(jsonObject2.getString("game_name"));
                gameInfo.setGame_size(jsonObject2.getString("game_size"));

                gameInfo.setGame_download_nums(jsonObject2.getString("game_download_nums"));
                gameInfo.setGame_stage(jsonObject2.getString("game_stage"));
                gameInfo.setGame_download_url(jsonObject2.getString("game_download_url"));
                gameInfo.setGame_id(jsonObject2.getString("game_id"));
                gameInfo.setGame_img(jsonObject2.getString("game_img"));
                gameInfo.setGame_platform(jsonObject2.getString("game_platform"));

                gameDetialParser.setCreate_time(jsonObject2.getString("create_time"));
                gameDetialParser.setGame_intro(jsonObject2.getString("game_intro"));
                gameDetialParser.setGame_test_nums(jsonObject2.getString("game_test_nums"));
                gameDetialParser.setGame_update_intro(jsonObject2.getString("game_update_intro"));
                gameDetialParser.setIs_care(jsonObject2.getInt("is_care"));
                gameDetialParser.setScore((float) jsonObject2.getDouble("score"));
                gameDetialParser.setGameInfo(gameInfo);

                JSONObject cp=jsonObject1.getJSONObject("cpInfo");
                GameDetialParser.CpInfo cpInfo=gameDetialParser.new CpInfo();
                cpInfo.setCity_id(cp.getString("city_id"));
                cpInfo.setCompany_scale(cp.getString("company_scale"));
                cpInfo.setCompany_site(cp.getString("company_site"));
                cpInfo.setCompay_phone(cp.getString("company_phone"));
                cpInfo.setRequire(cp.getString("require"));

                gameDetialParser.setCpInfo(cpInfo);


                JSONArray shots=jsonObject2.getJSONArray("game_shot");
                List<String> game_shots=new ArrayList<>();
                for (int i=0;i<shots.length();i++){
                    String imgs=shots.getString(i);
                    game_shots.add(imgs);
                }
                gameDetialParser.setGame_shot(game_shots);
                gameDetialParser.setStaticUrl(jsonObject1.getString("staticUrl"));
                gameDetialParser.setWebUrl(jsonObject1.getString("webUrl"));

                JSONArray cpGame=jsonObject1.getJSONArray("cpGame");
                List<GameInfo> game_list=new ArrayList<>();

                for (int i=0;i<cpGame.length();i++){
                    JSONObject jsonObject3=cpGame.getJSONObject(i);
                    GameInfo gameInfo1=new GameInfo();
                    gameInfo1.setGame_id(jsonObject3.getString("game_id"));
                    gameInfo1.setGame_img(jsonObject3.getString("game_img"));
                    gameInfo1.setGame_name(jsonObject3.getString("game_name"));
                    game_list.add(gameInfo1);
                }
                gameDetialParser.setCpGame(game_list);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return gameDetialParser;
    }

}

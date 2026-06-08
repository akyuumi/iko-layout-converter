package jp.co.iko.layoutconverter.task.sample;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * サンプル顧客変換 Task の MyBatis Mapper。
 */
@Mapper
public interface SampleCustomerMapper {

    /**
     * 移行元サンプル顧客テーブルの件数を取得する。
     *
     * @param sourceTable001 スキーマ修飾済みの移行元テーブル名
     * @return 読込対象件数
     */
    long countSource(@Param("sourceTable001") String sourceTable001);

    /**
     * 移行元サンプル顧客テーブルを LEFT JOIN し、移行先サンプル顧客テーブルへ MERGE する。
     *
     * @param sourceTable001 スキーマ修飾済みの移行元テーブル名 1
     * @param sourceTable002 スキーマ修飾済みの移行元テーブル名 2
     * @param targetTable スキーマ修飾済みの移行先テーブル名
     * @return MERGE 件数
     */
    int mergeCustomers(
            @Param("sourceTable001") String sourceTable001,
            @Param("sourceTable002") String sourceTable002,
            @Param("targetTable") String targetTable
    );
}

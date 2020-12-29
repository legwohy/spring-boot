package com.cobra.service;

import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author admin
 * @date 2020/12/24 14:14
 * @desc
 */
public class OptionalTest {

    /**
     * 替换深度空值判断
     * 1、对象包装
     */
    @Test
    public void testWrapper(){
        Province province = new Province();
        if (null != province) {
            City city = province.getCity();
            if (null != city) {
                Country country = city.getCountry();
                if (null != country) {
                    if (null != country.getName()) {
                        country.setName("test");
                    }
                }

            }
        }

        // 深度默认值
        TmpUser user = new TmpUser();
        Assert.assertEquals("default",
                        user.wrapperAddress()
                                        .flatMap(Address::wrapperProvince)// flatMap 返回对象 与filter一样
                                        .flatMap(Province::wrapCity)
                                        .flatMap(City::wrapCountry)
                                        .map(country -> country.getName())// map 返回字段的结果
                                        .orElse("default")// 默认值
        );

        // 深度空值判断
        Assert.assertFalse(user.wrapperAddress()
                        .flatMap(Address::wrapperProvince)
                        .flatMap(Province::wrapCity)
                        .flatMap(City::wrapCountry)
                        .isPresent());// 存在值
    }

    @Test
    public void testStream2Option(){
        Stream<String> names = Stream.of("A", "AB", "C", "D", "E");
        Optional<String> optional = names.filter(name -> name.startsWith("A")).findFirst();
        Assert.assertTrue(optional.isPresent());

    }

    @Data
    private class TmpUser {
        private Address address;

        public Optional<Address> wrapperAddress(){
            return Optional.ofNullable(address);
        }
    }

    @Data
    private class Address {
        private Province province;

        public Optional<Province> wrapperProvince(){
            return Optional.ofNullable(province);
        }
    }

    @Data
    private class Province {
        private Integer id;
        private City city;

        public Optional<City> wrapCity(){
            return Optional.ofNullable(city);
        }
    }

    @Data
    private class City {
        private Integer id;
        private Country country;

        public Optional<Country> wrapCountry(){
            return Optional.ofNullable(country);
        }
    }

    @Data
    private class Country {
        private String code;
        private String name;
    }

}

package com.pouillos.monpilulier.enumeration;

public enum QuantiteDose {
    //Objets directement construits
    ZeroAndQuarter("0,25",0.25),
    ZeroAndHalf("0,5",0.5),
    ZeroAndThreeQuarters("0,75",0.75),
    One("1",1.00),
    OneAndQuarter("1,25",1.25),
    OneAndHalf("1,5",1.5),
    OneAndThreeQuarters("1,75",1.75),
    Two("2",2.00),
    TwoAndQuarter("2,25",2.25),
    TwoAndHalf("2,5",2.5),
    TwoAndThreeQuarters("2,75",2.75),
    Three("3",3.00),
    ThreeAndQuarter("3,25",3.25),
    ThreeAndHalf("3,5",3.5),
    ThreeAndThreeQuarters("3,75",3.75),
    Four("4",4.00),
    FourAndQuarter("4,25",4.25),
    FourAndHalf("4,5",4.5),
    FourAndThreeQuarters("4,75",4.75),
    Five("5",5.00),
    FiveAndQuarter("5,25",5.25),
    FiveAndHalf("5,5",5.5),
    FiveAndThreeQuarters("5,75",5.75),
    Six("6",6.00),
    SixAndQuarter("6,25",6.25),
    SixAndHalf("6,5",6.5),
    SixAndThreeQuarters("6,75",6.75),
    Seven("7",7.00),
    SevenAndQuarter("7,25",7.25),
    SevenAndHalf("7,5",7.5),
    SevenAndThreeQuarters("7,75",7.75),
    Eight("8",8.00),
    EightAndQuarter("8,25",8.25),
    EightAndHalf("8,5",8.5),
    EightAndThreeQuarters("8,75",8.75),
    Nine("9",9.00),
    NineAndQuarter("9,25",9.25),
    NineAndHalf("9,5",9.5),
    NineAndThreeQuarters("9,75",9.75),
    Ten("10",10.00),
    TenAndQuarter("10,25",10.25),
    TenAndHalf("10,5",10.5),
    TenAndThreeQuarters("10,75",10.75),
    Eleven("11",11.00),
    ElevenAndQuarter("11,25",11.25),
    ElevenAndHalf("11,5",11.5),
    ElevenAndThreeQuarters("11,75",11.75),
    Twelve("12",12.00),
    TwelveAndQuarter("12,25",12.25),
    TwelveAndHalf("12,5",12.5),
    TwelveAndThreeQuarters("12,75",12.75),
    Thirteen("13",13.00),
    ThirteenAndQuarter("13,25",13.25),
    ThirteenAndHalf("13,5",13.5),
    ThirteenAndThreeQuarters("13,75",13.75),
    Fourteen("14",14.00),
    FourteenAndQuarter("14,25",14.25),
    FourteenAndHalf("14,5",14.5),
    FourteenAndThreeQuarters("14,75",14.75),
    Fifteen("15",15.00),
    FifteenAndQuarter("15,25",15.25),
    FifteenAndHalf("15,5",15.5),
    FifteenAndThreeQuarters("15,75",15.75);



    private String name;
    private Double number;

    //Constructeur
    QuantiteDose(String name,Double number){
        this.name = name;
        this.number = number;
    }

    public String toString(){
        return number.toString();
    }

}

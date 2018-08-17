create table crimes2
(
        IncidentID  varchar,
        OffenceCode varchar,
        CRNumber varchar,
        DispatchDateTime varchar,
        NIBRSCode varchar,
        Victims varchar,
        CrimeName1 varchar,
        CrimeName2 varchar,
        CrimeName3 varchar,
        PoliceDistrictName varchar,
        BlockAddress varchar,
        City varchar,
        State varchar,
        ZipCode varchar,
        Agency varchar,
        Place varchar,
        Sector varchar,
        Beat varchar,
        PRA varchar,
        AddressNumber varchar,
        StartDateTime varchar,
        EndDateTime varchar,
        Latitude varchar,
        Longitude varchar,
        PoliceDistrictNumber varchar,
        Location varchar,
        primary key (IncidentID, Crimename1,Crimename2,crimename3, (city))
);
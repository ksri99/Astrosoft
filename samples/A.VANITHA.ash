<?xml version="1.0" encoding="UTF-8"?>
<!-- persistence.xml schema -->
<xsd:schema targetNamespace="http://java.sun.com/xml/ns/persistence" 
  xmlns:xsd="http://www.w3.org/2001/XMLSchema"
  xmlns:persistence="http://java.sun.com/xml/ns/persistence"
  elementFormDefault="qualified" 
  attributeFormDefault="unqualified" 
  version="1.0">

  <xsd:annotation>
    <xsd:documentation>
      @(#)persistence_1_0.xsd  1.0  Feb 9 2006
    </xsd:documentation>
  </xsd:annotation>
   <xsd:annotation>
     <xsd:documentation><![CDATA[

     This is the XML Schema for the persistence configuration file.
     The file must be named "META-INF/persistence.xml" in the 
     persistence archive.
     Persistence configuration files must indicate
     the persistence schema by using the persistence namespace:

     http://java.sun.com/xml/ns/persistence

     and indicate the version of the schema by
     using the version element as shown below:

      <persistence xmlns="http://java.sun.com/xml/ns/persistence"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://java.sun.com/xml/ns/persistence
          http://java.sun.com/xml/ns/persistence/persistence_1_0.xsd"
        version="1.0">
          ...
      </persistence>

    ]]></xsd:documentation>
  </xsd:annotation>

  <xsd:simpleType name="versionType">
    <xsd:restriction base="xsd:token">
      <xsd:pattern value="[0-9]+(\.[0-9]+)*"/>
    </xsd:restriction>
  </xsd:simpleType>

  <!-- **************************************************** -->

  <xsd:element name="persistence">
    <xsd:complexType>
      <xsd:sequence>

        <!-- **************************************************** -->

        <xsd:element name="persistence-unit" 
                     minOccurs="0" maxOccurs="unbounded">
          <xsd:complexType>
            <xsd:annotation>
              <xsd:documentation>

                Configuration of a persistence unit.

              </xsd:documentation>
            </xsd:annotation>
            <xsd:sequence>

            <!-- **************************************************** -->

              <xsd:element name="description" type="xsd:string" 
                           minOccurs="0">
                <xsd:annotation>
                  <xsd:documentation>

                    Textual description of this persistence unit.

                  </xsd:documentation>
                </xsd:annotation>
              </xsd:element>

              <!-- **************************************************** -->

              <xsd:element name="provider" type="xsd:string" 
                           minOccurs="0">
                <xsd:annotation>
                  <xsd:documentation>

                    Provider class that supplies EntityManagers for this 
                    persistence unit.

                  </xsd:documentation>
                </xsd:annotation>
              </xsd:element>

              <!-- **************************************************** -->

              <xsd:element name="jta-data-source" type="xsd:string" 
                           minOccurs="0">
                <xsd:annotation>
                  <xsd:documentation>

                    The container-specific name of the JTA datasource to use.

                  </xsd:documentation>
                </xsd:annotation>
              </xsd:element>

              <!-- **************************************************** -->

              <xsd:element name="non-jta-data-source" type="xsd:string" 
                           minOccurs="0">
                <xsd:annotation>
                  <xsd:documentation>

                    The container-specific name of a non-JTA datasource to use.

                  </xsd:documentation>
                </xsd:annotation>
              </xsd:element>

              <!-- **************************************************** -->

              <xsd:element name="mapping-file" type="xsd:string" 
                           minOccurs="0" maxOccurs="unbounded">
                <xsd:annotation>
                  <xsd:documentation>

                    File containing mapping information. Loaded as a resource 
                    by the persistence provider.

                  </xsd:documentation>
                </xsd:annotation>
              </xsd:element>

              <!-- **************************************************** -->

              <xsd:element name="jar-file" type="xsd:string" 
                           minOccurs="0" maxOccurs="unbounded">
                <xsd:annotation>
                  <xsd:documentation>

                    Jar file that should be scanned for entities. 
                    Not applicable to Java SE persistence units.

                  </xsd:documentation>
                </xsd:annotation>
              </xsd:element>

              <!-- **************************************************** -->

              <xsd:element name="class" type="xsd:string" 
                           minOccurs="0" maxOccurs="unbounded">
                <xsd:annotation>
                  <xsd:documentation>

                    Class to scan for annotations.  It should be annotated 
                    with either @Entity, @Embeddable or @MappedSuperclass.

                  </xsd:documentation>
                </xsd:annotation>
              </xsd:element>

              <!-- **************************************************** -->

              <xsd:element name="exclude-unlisted-classes" type="xsd:boolean" 
                           default="false" minOccurs="0">
                <xsd:annotation>
                  <xsd:documentation>

                    When set to true then only listed classes and jars will 
                    be scanned for persistent classes, otherwise the enclosing 
                    jar or directory will also be scanned. Not applicable to 
                    Java SE persistence units.

                  </xsd:documentation>
                </xsd:annotation>
              </xsd:element>

              <!-- **************************************************** -->

              <xsd:element name="properties" minOccurs="0">
                <xsd:annotation>
                  <xsd:documentation>

                    A list of vendor-specific properties.

                  </xsd:documentation>
                </xsd:annotation>
                <xsd:complexType>
                  <xsd:sequence>
                    <xsd:element name="property" 
                                 minOccurs="0" maxOccurs="unbounded">
                      <xsd:annotation>
                        <xsd:documentation>
                          A name-value pair.
                        </xsd:documentation>
                      </xsd:annotation>
                      <xsd:complexType>
                        <xsd:attribute name="name" type="xsd:string" 
                                       use="required"/>
                        <xsd:attribute name="value" type="xsd:string" 
                                       use="required"/>
                      </xsd:complexType>
                    </xsd:element>
                  </xsd:sequence>
                </xsd:complexType>
              </xsd:element>

            </xsd:sequence>

            <!-- **************************************************** -->

            <xsd:attribute name="name" type="xsd:string" use="required">
              <xsd:annotation>
                <xsd:documentation>

                  Name used in code to reference this persistence unit.

                </xsd:documentation>
              </xsd:annotation>
            </xsd:attribute>

            <!-- **************************************************** -->

            <xsd:attribute name="transaction-type" 
                           type="persistence:persistence-unit-transaction-type">
              <xsd:annotation>
                <xsd:documentation>

                  Type of transactions used by EntityManagers from this 
                  persistence unit.

                </xsd:documentation>
              </xsd:annotation>
            </xsd:attribute>

          </xsd:complexType>
        </xsd:element>
      </xsd:sequence>
      <xsd:attribute name="version" type="persistence:versionType" 
                     fixed="1.0" use="required"/>
    </xsd:complexType>
  </xsd:element>

  <!-- **************************************************** -->

  <xsd:simpleType name="persistence-unit-transaction-type">
    <xsd:annotation>
      <xsd:documentation>

        public enum TransactionType { JTA, RESOURCE_LOCAL };

      </xsd:documentation>
    </xsd:annotation>
    <xsd:restriction base="xsd:token">
      <xsd:enumeration value="JTA"/>
      <xsd:enumeration value="RESOURCE_LOCAL"/>
    </xsd:restriction>
  </xsd:simpleType>

</xsd:schema>

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
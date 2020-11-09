/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.przemarcz.avro;

import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.SchemaStore;
import org.apache.avro.specific.SpecificData;

@org.apache.avro.specific.AvroGenerated
public class OrderAvro extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
    public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"OrderAvro\",\"namespace\":\"com.przemarcz.avro\",\"fields\":[{\"name\":\"id\",\"type\":\"string\",\"logicalType\":\"UUID\"},{\"name\":\"forename\",\"type\":\"string\"},{\"name\":\"surname\",\"type\":\"string\"},{\"name\":\"street\",\"type\":\"string\"},{\"name\":\"city\",\"type\":\"string\"},{\"name\":\"phoneNumber\",\"type\":\"string\"},{\"name\":\"postCode\",\"type\":\"string\"},{\"name\":\"comment\",\"type\":\"string\"},{\"name\":\"restaurantId\",\"type\":\"string\"},{\"name\":\"meals\",\"type\":[\"null\",{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"MealAvro\",\"fields\":[{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"price\",\"type\":[{\"type\":\"string\",\"java-class\":\"java.math.BigDecimal\"}]},{\"name\":\"quantity\",\"type\":\"string\"},{\"name\":\"image\",\"type\":\"string\"},{\"name\":\"ingredients\",\"type\":\"string\"},{\"name\":\"timeToDo\",\"type\":[{\"type\":\"string\",\"java-class\":\"java.math.BigDecimal\"}]}]}}],\"default\":null}]}");
    private static final long serialVersionUID = -2177099189637877564L;
    private static final SpecificData MODEL$ = new SpecificData();
    private static final BinaryMessageEncoder<OrderAvro> ENCODER =
            new BinaryMessageEncoder<OrderAvro>(MODEL$, SCHEMA$);
    private static final BinaryMessageDecoder<OrderAvro> DECODER =
      new BinaryMessageDecoder<OrderAvro>(MODEL$, SCHEMA$);

    static {
        MODEL$.addLogicalTypeConversion(new org.apache.avro.Conversions.DecimalConversion());
    }

    private java.lang.CharSequence id;

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<OrderAvro> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<OrderAvro> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<OrderAvro> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<OrderAvro>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this OrderAvro to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a OrderAvro from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a OrderAvro instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static OrderAvro fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }
    private java.lang.CharSequence forename;
    /**
     * All-args constructor.
     *
     * @param id           The new value for id
     * @param forename     The new value for forename
     * @param surname      The new value for surname
     * @param street       The new value for street
     * @param city         The new value for city
     * @param phoneNumber  The new value for phoneNumber
     * @param postCode     The new value for postCode
     * @param comment      The new value for comment
     * @param restaurantId The new value for restaurantId
     * @param meals        The new value for meals
     */
    public OrderAvro(java.lang.CharSequence id, java.lang.CharSequence forename, java.lang.CharSequence surname, java.lang.CharSequence street, java.lang.CharSequence city, java.lang.CharSequence phoneNumber, java.lang.CharSequence postCode, java.lang.CharSequence comment, java.lang.CharSequence restaurantId, java.util.List<com.przemarcz.avro.MealAvro> meals) {
        this.id = id;
        this.forename = forename;
        this.surname = surname;
        this.street = street;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.postCode = postCode;
        this.comment = comment;
        this.restaurantId = restaurantId;
        this.meals = meals;
    }
   private java.lang.CharSequence surname;
   private java.lang.CharSequence street;
   private java.lang.CharSequence city;
   private java.lang.CharSequence phoneNumber;
   private java.lang.CharSequence postCode;
   private java.lang.CharSequence comment;
   private java.lang.CharSequence restaurantId;
   private java.util.List<com.przemarcz.avro.MealAvro> meals;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public OrderAvro() {}

    public static org.apache.avro.Schema getClassSchema() {
        return SCHEMA$;
    }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }

  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
      switch (field$) {
          case 0:
              return id;
          case 1:
              return forename;
          case 2:
              return surname;
          case 3:
              return street;
          case 4:
              return city;
          case 5:
              return phoneNumber;
          case 6:
              return postCode;
          case 7:
              return comment;
          case 8:
              return restaurantId;
          case 9:
              return meals;
          default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
      switch (field$) {
          case 0:
              id = (java.lang.CharSequence) value$;
              break;
          case 1:
              forename = (java.lang.CharSequence) value$;
              break;
          case 2:
              surname = (java.lang.CharSequence) value$;
              break;
          case 3:
              street = (java.lang.CharSequence) value$;
              break;
          case 4:
              city = (java.lang.CharSequence) value$;
              break;
          case 5:
              phoneNumber = (java.lang.CharSequence) value$;
              break;
          case 6:
              postCode = (java.lang.CharSequence) value$;
              break;
          case 7:
              comment = (java.lang.CharSequence) value$;
              break;
          case 8:
              restaurantId = (java.lang.CharSequence) value$;
              break;
          case 9:
              meals = (java.util.List<com.przemarcz.avro.MealAvro>) value$;
              break;
          default:
              throw new IndexOutOfBoundsException("Invalid index: " + field$);
      }
  }

    /**
     * Gets the value of the 'id' field.
     *
     * @return The value of the 'id' field.
     */
    public java.lang.CharSequence getId() {
        return id;
    }


    /**
     * Sets the value of the 'id' field.
     *
     * @param value the value to set.
     */
    public void setId(java.lang.CharSequence value) {
        this.id = value;
    }

    /**
     * Gets the value of the 'forename' field.
     * @return The value of the 'forename' field.
   */
  public java.lang.CharSequence getForename() {
    return forename;
  }


  /**
   * Sets the value of the 'forename' field.
   * @param value the value to set.
   */
  public void setForename(java.lang.CharSequence value) {
    this.forename = value;
  }

  /**
   * Gets the value of the 'surname' field.
   * @return The value of the 'surname' field.
   */
  public java.lang.CharSequence getSurname() {
    return surname;
  }


  /**
   * Sets the value of the 'surname' field.
   * @param value the value to set.
   */
  public void setSurname(java.lang.CharSequence value) {
    this.surname = value;
  }

  /**
   * Gets the value of the 'street' field.
   * @return The value of the 'street' field.
   */
  public java.lang.CharSequence getStreet() {
    return street;
  }


  /**
   * Sets the value of the 'street' field.
   * @param value the value to set.
   */
  public void setStreet(java.lang.CharSequence value) {
    this.street = value;
  }

  /**
   * Gets the value of the 'city' field.
   * @return The value of the 'city' field.
   */
  public java.lang.CharSequence getCity() {
    return city;
  }


  /**
   * Sets the value of the 'city' field.
   * @param value the value to set.
   */
  public void setCity(java.lang.CharSequence value) {
    this.city = value;
  }

  /**
   * Gets the value of the 'phoneNumber' field.
   * @return The value of the 'phoneNumber' field.
   */
  public java.lang.CharSequence getPhoneNumber() {
    return phoneNumber;
  }


  /**
   * Sets the value of the 'phoneNumber' field.
   * @param value the value to set.
   */
  public void setPhoneNumber(java.lang.CharSequence value) {
    this.phoneNumber = value;
  }

  /**
   * Gets the value of the 'postCode' field.
   * @return The value of the 'postCode' field.
   */
  public java.lang.CharSequence getPostCode() {
    return postCode;
  }


  /**
   * Sets the value of the 'postCode' field.
   * @param value the value to set.
   */
  public void setPostCode(java.lang.CharSequence value) {
    this.postCode = value;
  }

  /**
   * Gets the value of the 'comment' field.
   * @return The value of the 'comment' field.
   */
  public java.lang.CharSequence getComment() {
    return comment;
  }


  /**
   * Sets the value of the 'comment' field.
   * @param value the value to set.
   */
  public void setComment(java.lang.CharSequence value) {
    this.comment = value;
  }

  /**
   * Gets the value of the 'restaurantId' field.
   * @return The value of the 'restaurantId' field.
   */
  public java.lang.CharSequence getRestaurantId() {
    return restaurantId;
  }


  /**
   * Sets the value of the 'restaurantId' field.
   * @param value the value to set.
   */
  public void setRestaurantId(java.lang.CharSequence value) {
    this.restaurantId = value;
  }

  /**
   * Gets the value of the 'meals' field.
   * @return The value of the 'meals' field.
   */
  public java.util.List<com.przemarcz.avro.MealAvro> getMeals() {
    return meals;
  }


  /**
   * Sets the value of the 'meals' field.
   * @param value the value to set.
   */
  public void setMeals(java.util.List<com.przemarcz.avro.MealAvro> value) {
    this.meals = value;
  }

  /**
   * Creates a new OrderAvro RecordBuilder.
   * @return A new OrderAvro RecordBuilder
   */
  public static com.przemarcz.avro.OrderAvro.Builder newBuilder() {
    return new com.przemarcz.avro.OrderAvro.Builder();
  }

  /**
   * Creates a new OrderAvro RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new OrderAvro RecordBuilder
   */
  public static com.przemarcz.avro.OrderAvro.Builder newBuilder(com.przemarcz.avro.OrderAvro.Builder other) {
    if (other == null) {
      return new com.przemarcz.avro.OrderAvro.Builder();
    } else {
      return new com.przemarcz.avro.OrderAvro.Builder(other);
    }
  }

  /**
   * Creates a new OrderAvro RecordBuilder by copying an existing OrderAvro instance.
   * @param other The existing instance to copy.
   * @return A new OrderAvro RecordBuilder
   */
  public static com.przemarcz.avro.OrderAvro.Builder newBuilder(com.przemarcz.avro.OrderAvro other) {
    if (other == null) {
      return new com.przemarcz.avro.OrderAvro.Builder();
    } else {
      return new com.przemarcz.avro.OrderAvro.Builder(other);
    }
  }

  /**
   * RecordBuilder for OrderAvro instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<OrderAvro>
          implements org.apache.avro.data.RecordBuilder<OrderAvro> {

    private java.lang.CharSequence id;
    private java.lang.CharSequence forename;
    private java.lang.CharSequence surname;
    private java.lang.CharSequence street;
    private java.lang.CharSequence city;
    private java.lang.CharSequence phoneNumber;
    private java.lang.CharSequence postCode;
    private java.lang.CharSequence comment;
    private java.lang.CharSequence restaurantId;
    private java.util.List<com.przemarcz.avro.MealAvro> meals;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.przemarcz.avro.OrderAvro.Builder other) {
        super(other);
        if (isValidValue(fields()[0], other.id)) {
            this.id = data().deepCopy(fields()[0].schema(), other.id);
            fieldSetFlags()[0] = other.fieldSetFlags()[0];
        }
        if (isValidValue(fields()[1], other.forename)) {
            this.forename = data().deepCopy(fields()[1].schema(), other.forename);
            fieldSetFlags()[1] = other.fieldSetFlags()[1];
        }
        if (isValidValue(fields()[2], other.surname)) {
            this.surname = data().deepCopy(fields()[2].schema(), other.surname);
            fieldSetFlags()[2] = other.fieldSetFlags()[2];
        }
        if (isValidValue(fields()[3], other.street)) {
            this.street = data().deepCopy(fields()[3].schema(), other.street);
            fieldSetFlags()[3] = other.fieldSetFlags()[3];
        }
        if (isValidValue(fields()[4], other.city)) {
            this.city = data().deepCopy(fields()[4].schema(), other.city);
            fieldSetFlags()[4] = other.fieldSetFlags()[4];
        }
        if (isValidValue(fields()[5], other.phoneNumber)) {
            this.phoneNumber = data().deepCopy(fields()[5].schema(), other.phoneNumber);
            fieldSetFlags()[5] = other.fieldSetFlags()[5];
        }
        if (isValidValue(fields()[6], other.postCode)) {
            this.postCode = data().deepCopy(fields()[6].schema(), other.postCode);
            fieldSetFlags()[6] = other.fieldSetFlags()[6];
        }
        if (isValidValue(fields()[7], other.comment)) {
            this.comment = data().deepCopy(fields()[7].schema(), other.comment);
            fieldSetFlags()[7] = other.fieldSetFlags()[7];
        }
        if (isValidValue(fields()[8], other.restaurantId)) {
            this.restaurantId = data().deepCopy(fields()[8].schema(), other.restaurantId);
            fieldSetFlags()[8] = other.fieldSetFlags()[8];
        }
        if (isValidValue(fields()[9], other.meals)) {
            this.meals = data().deepCopy(fields()[9].schema(), other.meals);
            fieldSetFlags()[9] = other.fieldSetFlags()[9];
        }
    }

    /**
     * Creates a Builder by copying an existing OrderAvro instance
     * @param other The existing instance to copy.
     */
    private Builder(com.przemarcz.avro.OrderAvro other) {
        super(SCHEMA$);
        if (isValidValue(fields()[0], other.id)) {
            this.id = data().deepCopy(fields()[0].schema(), other.id);
            fieldSetFlags()[0] = true;
        }
        if (isValidValue(fields()[1], other.forename)) {
            this.forename = data().deepCopy(fields()[1].schema(), other.forename);
            fieldSetFlags()[1] = true;
        }
        if (isValidValue(fields()[2], other.surname)) {
            this.surname = data().deepCopy(fields()[2].schema(), other.surname);
            fieldSetFlags()[2] = true;
        }
        if (isValidValue(fields()[3], other.street)) {
            this.street = data().deepCopy(fields()[3].schema(), other.street);
            fieldSetFlags()[3] = true;
        }
        if (isValidValue(fields()[4], other.city)) {
            this.city = data().deepCopy(fields()[4].schema(), other.city);
            fieldSetFlags()[4] = true;
        }
        if (isValidValue(fields()[5], other.phoneNumber)) {
            this.phoneNumber = data().deepCopy(fields()[5].schema(), other.phoneNumber);
            fieldSetFlags()[5] = true;
        }
        if (isValidValue(fields()[6], other.postCode)) {
            this.postCode = data().deepCopy(fields()[6].schema(), other.postCode);
            fieldSetFlags()[6] = true;
        }
        if (isValidValue(fields()[7], other.comment)) {
            this.comment = data().deepCopy(fields()[7].schema(), other.comment);
            fieldSetFlags()[7] = true;
        }
        if (isValidValue(fields()[8], other.restaurantId)) {
            this.restaurantId = data().deepCopy(fields()[8].schema(), other.restaurantId);
            fieldSetFlags()[8] = true;
        }
        if (isValidValue(fields()[9], other.meals)) {
            this.meals = data().deepCopy(fields()[9].schema(), other.meals);
            fieldSetFlags()[9] = true;
        }
    }

      /**
       * Gets the value of the 'id' field.
       *
       * @return The value.
       */
      public java.lang.CharSequence getId() {
          return id;
      }


      /**
       * Sets the value of the 'id' field.
       *
       * @param value The value of 'id'.
       * @return This builder.
       */
      public com.przemarcz.avro.OrderAvro.Builder setId(java.lang.CharSequence value) {
          validate(fields()[0], value);
          this.id = value;
          fieldSetFlags()[0] = true;
          return this;
      }

      /**
       * Checks whether the 'id' field has been set.
       *
       * @return True if the 'id' field has been set, false otherwise.
       */
      public boolean hasId() {
          return fieldSetFlags()[0];
      }


      /**
       * Clears the value of the 'id' field.
       *
       * @return This builder.
       */
      public com.przemarcz.avro.OrderAvro.Builder clearId() {
          id = null;
          fieldSetFlags()[0] = false;
          return this;
      }

      /**
       * Gets the value of the 'forename' field.
       * @return The value.
      */
    public java.lang.CharSequence getForename() {
      return forename;
    }


    /**
      * Sets the value of the 'forename' field.
     * @param value The value of 'forename'.
     * @return This builder.
     */
    public com.przemarcz.avro.OrderAvro.Builder setForename(java.lang.CharSequence value) {
      validate(fields()[1], value);
      this.forename = value;
      fieldSetFlags()[1] = true;
      return this;
    }

      /**
       * Checks whether the 'forename' field has been set.
      * @return True if the 'forename' field has been set, false otherwise.
      */
    public boolean hasForename() {
      return fieldSetFlags()[1];
    }


      /**
       * Clears the value of the 'forename' field.
      * @return This builder.
      */
    public com.przemarcz.avro.OrderAvro.Builder clearForename() {
      forename = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'surname' field.
      * @return The value.
      */
    public java.lang.CharSequence getSurname() {
      return surname;
    }


    /**
      * Sets the value of the 'surname' field.
     * @param value The value of 'surname'.
     * @return This builder.
     */
    public com.przemarcz.avro.OrderAvro.Builder setSurname(java.lang.CharSequence value) {
      validate(fields()[2], value);
      this.surname = value;
      fieldSetFlags()[2] = true;
      return this;
    }

      /**
       * Checks whether the 'surname' field has been set.
      * @return True if the 'surname' field has been set, false otherwise.
      */
    public boolean hasSurname() {
      return fieldSetFlags()[2];
    }


      /**
       * Clears the value of the 'surname' field.
      * @return This builder.
      */
    public com.przemarcz.avro.OrderAvro.Builder clearSurname() {
      surname = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'street' field.
      * @return The value.
      */
    public java.lang.CharSequence getStreet() {
      return street;
    }


    /**
      * Sets the value of the 'street' field.
     * @param value The value of 'street'.
     * @return This builder.
     */
    public com.przemarcz.avro.OrderAvro.Builder setStreet(java.lang.CharSequence value) {
      validate(fields()[3], value);
      this.street = value;
      fieldSetFlags()[3] = true;
      return this;
    }

      /**
       * Checks whether the 'street' field has been set.
      * @return True if the 'street' field has been set, false otherwise.
      */
    public boolean hasStreet() {
      return fieldSetFlags()[3];
    }


      /**
       * Clears the value of the 'street' field.
      * @return This builder.
      */
    public com.przemarcz.avro.OrderAvro.Builder clearStreet() {
      street = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
      * Gets the value of the 'city' field.
      * @return The value.
      */
    public java.lang.CharSequence getCity() {
      return city;
    }


    /**
      * Sets the value of the 'city' field.
     * @param value The value of 'city'.
     * @return This builder.
     */
    public com.przemarcz.avro.OrderAvro.Builder setCity(java.lang.CharSequence value) {
      validate(fields()[4], value);
      this.city = value;
      fieldSetFlags()[4] = true;
      return this;
    }

      /**
       * Checks whether the 'city' field has been set.
      * @return True if the 'city' field has been set, false otherwise.
      */
    public boolean hasCity() {
      return fieldSetFlags()[4];
    }


      /**
       * Clears the value of the 'city' field.
      * @return This builder.
      */
    public com.przemarcz.avro.OrderAvro.Builder clearCity() {
      city = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    /**
      * Gets the value of the 'phoneNumber' field.
      * @return The value.
      */
    public java.lang.CharSequence getPhoneNumber() {
      return phoneNumber;
    }


    /**
      * Sets the value of the 'phoneNumber' field.
     * @param value The value of 'phoneNumber'.
     * @return This builder.
     */
    public com.przemarcz.avro.OrderAvro.Builder setPhoneNumber(java.lang.CharSequence value) {
      validate(fields()[5], value);
      this.phoneNumber = value;
      fieldSetFlags()[5] = true;
      return this;
    }

    /**
     * Checks whether the 'phoneNumber' field has been set.
      * @return True if the 'phoneNumber' field has been set, false otherwise.
      */
    public boolean hasPhoneNumber() {
      return fieldSetFlags()[5];
    }


      /**
       * Clears the value of the 'phoneNumber' field.
      * @return This builder.
      */
    public com.przemarcz.avro.OrderAvro.Builder clearPhoneNumber() {
      phoneNumber = null;
      fieldSetFlags()[5] = false;
      return this;
    }

    /**
      * Gets the value of the 'postCode' field.
      * @return The value.
      */
    public java.lang.CharSequence getPostCode() {
      return postCode;
    }


    /**
      * Sets the value of the 'postCode' field.
     * @param value The value of 'postCode'.
     * @return This builder.
     */
    public com.przemarcz.avro.OrderAvro.Builder setPostCode(java.lang.CharSequence value) {
      validate(fields()[6], value);
      this.postCode = value;
      fieldSetFlags()[6] = true;
      return this;
    }

      /**
       * Checks whether the 'postCode' field has been set.
      * @return True if the 'postCode' field has been set, false otherwise.
      */
    public boolean hasPostCode() {
      return fieldSetFlags()[6];
    }


      /**
       * Clears the value of the 'postCode' field.
      * @return This builder.
      */
    public com.przemarcz.avro.OrderAvro.Builder clearPostCode() {
      postCode = null;
      fieldSetFlags()[6] = false;
      return this;
    }

    /**
      * Gets the value of the 'comment' field.
      * @return The value.
      */
    public java.lang.CharSequence getComment() {
      return comment;
    }


    /**
      * Sets the value of the 'comment' field.
     * @param value The value of 'comment'.
     * @return This builder.
     */
    public com.przemarcz.avro.OrderAvro.Builder setComment(java.lang.CharSequence value) {
      validate(fields()[7], value);
      this.comment = value;
      fieldSetFlags()[7] = true;
      return this;
    }

      /**
       * Checks whether the 'comment' field has been set.
      * @return True if the 'comment' field has been set, false otherwise.
      */
    public boolean hasComment() {
      return fieldSetFlags()[7];
    }


      /**
       * Clears the value of the 'comment' field.
      * @return This builder.
      */
    public com.przemarcz.avro.OrderAvro.Builder clearComment() {
      comment = null;
      fieldSetFlags()[7] = false;
      return this;
    }

    /**
      * Gets the value of the 'restaurantId' field.
      * @return The value.
      */
    public java.lang.CharSequence getRestaurantId() {
      return restaurantId;
    }


    /**
      * Sets the value of the 'restaurantId' field.
     * @param value The value of 'restaurantId'.
     * @return This builder.
     */
    public com.przemarcz.avro.OrderAvro.Builder setRestaurantId(java.lang.CharSequence value) {
      validate(fields()[8], value);
      this.restaurantId = value;
      fieldSetFlags()[8] = true;
      return this;
    }

    /**
     * Checks whether the 'restaurantId' field has been set.
      * @return True if the 'restaurantId' field has been set, false otherwise.
      */
    public boolean hasRestaurantId() {
      return fieldSetFlags()[8];
    }


      /**
       * Clears the value of the 'restaurantId' field.
      * @return This builder.
      */
    public com.przemarcz.avro.OrderAvro.Builder clearRestaurantId() {
      restaurantId = null;
      fieldSetFlags()[8] = false;
      return this;
    }

    /**
      * Gets the value of the 'meals' field.
      * @return The value.
      */
    public java.util.List<com.przemarcz.avro.MealAvro> getMeals() {
      return meals;
    }


    /**
      * Sets the value of the 'meals' field.
      * @param value The value of 'meals'.
     * @return This builder.
     */
    public com.przemarcz.avro.OrderAvro.Builder setMeals(java.util.List<com.przemarcz.avro.MealAvro> value) {
      validate(fields()[9], value);
      this.meals = value;
      fieldSetFlags()[9] = true;
      return this;
    }

      /**
       * Checks whether the 'meals' field has been set.
      * @return True if the 'meals' field has been set, false otherwise.
      */
    public boolean hasMeals() {
      return fieldSetFlags()[9];
    }


      /**
       * Clears the value of the 'meals' field.
      * @return This builder.
      */
    public com.przemarcz.avro.OrderAvro.Builder clearMeals() {
        meals = null;
        fieldSetFlags()[9] = false;
        return this;
    }

      @Override
      @SuppressWarnings("unchecked")
      public OrderAvro build() {
          try {
              OrderAvro record = new OrderAvro();
              record.id = fieldSetFlags()[0] ? this.id : (java.lang.CharSequence) defaultValue(fields()[0]);
              record.forename = fieldSetFlags()[1] ? this.forename : (java.lang.CharSequence) defaultValue(fields()[1]);
              record.surname = fieldSetFlags()[2] ? this.surname : (java.lang.CharSequence) defaultValue(fields()[2]);
              record.street = fieldSetFlags()[3] ? this.street : (java.lang.CharSequence) defaultValue(fields()[3]);
              record.city = fieldSetFlags()[4] ? this.city : (java.lang.CharSequence) defaultValue(fields()[4]);
              record.phoneNumber = fieldSetFlags()[5] ? this.phoneNumber : (java.lang.CharSequence) defaultValue(fields()[5]);
              record.postCode = fieldSetFlags()[6] ? this.postCode : (java.lang.CharSequence) defaultValue(fields()[6]);
              record.comment = fieldSetFlags()[7] ? this.comment : (java.lang.CharSequence) defaultValue(fields()[7]);
              record.restaurantId = fieldSetFlags()[8] ? this.restaurantId : (java.lang.CharSequence) defaultValue(fields()[8]);
        record.meals = fieldSetFlags()[9] ? this.meals : (java.util.List<com.przemarcz.avro.MealAvro>) defaultValue(fields()[9]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<OrderAvro>
    WRITER$ = (org.apache.avro.io.DatumWriter<OrderAvro>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<OrderAvro>
    READER$ = (org.apache.avro.io.DatumReader<OrderAvro>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}











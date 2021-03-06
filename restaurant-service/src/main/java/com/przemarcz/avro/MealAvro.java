/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.przemarcz.avro;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class MealAvro extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 6796644829259329006L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"MealAvro\",\"namespace\":\"com.przemarcz.avro\",\"fields\":[{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"price\",\"type\":[{\"type\":\"string\",\"java-class\":\"java.math.BigDecimal\"}]},{\"name\":\"quantity\",\"type\":\"string\"},{\"name\":\"image\",\"type\":\"string\"},{\"name\":\"ingredients\",\"type\":\"string\"},{\"name\":\"timeToDo\",\"type\":[{\"type\":\"string\",\"java-class\":\"java.math.BigDecimal\"}]}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();
static {
    MODEL$.addLogicalTypeConversion(new org.apache.avro.Conversions.DecimalConversion());
  }

  private static final BinaryMessageEncoder<MealAvro> ENCODER =
      new BinaryMessageEncoder<MealAvro>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<MealAvro> DECODER =
      new BinaryMessageDecoder<MealAvro>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<MealAvro> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<MealAvro> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<MealAvro> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<MealAvro>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this MealAvro to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a MealAvro from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a MealAvro instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static MealAvro fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

   private java.lang.CharSequence name;
   private java.lang.Object price;
   private java.lang.CharSequence quantity;
   private java.lang.CharSequence image;
   private java.lang.CharSequence ingredients;
   private java.lang.Object timeToDo;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public MealAvro() {}

  /**
   * All-args constructor.
   * @param name The new value for name
   * @param price The new value for price
   * @param quantity The new value for quantity
   * @param image The new value for image
   * @param ingredients The new value for ingredients
   * @param timeToDo The new value for timeToDo
   */
  public MealAvro(java.lang.CharSequence name, java.lang.Object price, java.lang.CharSequence quantity, java.lang.CharSequence image, java.lang.CharSequence ingredients, java.lang.Object timeToDo) {
    this.name = name;
    this.price = price;
    this.quantity = quantity;
    this.image = image;
    this.ingredients = ingredients;
    this.timeToDo = timeToDo;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return name;
    case 1: return price;
    case 2: return quantity;
    case 3: return image;
    case 4: return ingredients;
    case 5: return timeToDo;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: name = (java.lang.CharSequence)value$; break;
    case 1: price = value$; break;
    case 2: quantity = (java.lang.CharSequence)value$; break;
    case 3: image = (java.lang.CharSequence)value$; break;
    case 4: ingredients = (java.lang.CharSequence)value$; break;
    case 5: timeToDo = value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'name' field.
   * @return The value of the 'name' field.
   */
  public java.lang.CharSequence getName() {
    return name;
  }


  /**
   * Sets the value of the 'name' field.
   * @param value the value to set.
   */
  public void setName(java.lang.CharSequence value) {
    this.name = value;
  }

  /**
   * Gets the value of the 'price' field.
   * @return The value of the 'price' field.
   */
  public java.lang.Object getPrice() {
    return price;
  }


  /**
   * Sets the value of the 'price' field.
   * @param value the value to set.
   */
  public void setPrice(java.lang.Object value) {
    this.price = value;
  }

  /**
   * Gets the value of the 'quantity' field.
   * @return The value of the 'quantity' field.
   */
  public java.lang.CharSequence getQuantity() {
    return quantity;
  }


  /**
   * Sets the value of the 'quantity' field.
   * @param value the value to set.
   */
  public void setQuantity(java.lang.CharSequence value) {
    this.quantity = value;
  }

  /**
   * Gets the value of the 'image' field.
   * @return The value of the 'image' field.
   */
  public java.lang.CharSequence getImage() {
    return image;
  }


  /**
   * Sets the value of the 'image' field.
   * @param value the value to set.
   */
  public void setImage(java.lang.CharSequence value) {
    this.image = value;
  }

  /**
   * Gets the value of the 'ingredients' field.
   * @return The value of the 'ingredients' field.
   */
  public java.lang.CharSequence getIngredients() {
    return ingredients;
  }


  /**
   * Sets the value of the 'ingredients' field.
   * @param value the value to set.
   */
  public void setIngredients(java.lang.CharSequence value) {
    this.ingredients = value;
  }

  /**
   * Gets the value of the 'timeToDo' field.
   * @return The value of the 'timeToDo' field.
   */
  public java.lang.Object getTimeToDo() {
    return timeToDo;
  }


  /**
   * Sets the value of the 'timeToDo' field.
   * @param value the value to set.
   */
  public void setTimeToDo(java.lang.Object value) {
    this.timeToDo = value;
  }

  /**
   * Creates a new MealAvro RecordBuilder.
   * @return A new MealAvro RecordBuilder
   */
  public static com.przemarcz.avro.MealAvro.Builder newBuilder() {
    return new com.przemarcz.avro.MealAvro.Builder();
  }

  /**
   * Creates a new MealAvro RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new MealAvro RecordBuilder
   */
  public static com.przemarcz.avro.MealAvro.Builder newBuilder(com.przemarcz.avro.MealAvro.Builder other) {
    if (other == null) {
      return new com.przemarcz.avro.MealAvro.Builder();
    } else {
      return new com.przemarcz.avro.MealAvro.Builder(other);
    }
  }

  /**
   * Creates a new MealAvro RecordBuilder by copying an existing MealAvro instance.
   * @param other The existing instance to copy.
   * @return A new MealAvro RecordBuilder
   */
  public static com.przemarcz.avro.MealAvro.Builder newBuilder(com.przemarcz.avro.MealAvro other) {
    if (other == null) {
      return new com.przemarcz.avro.MealAvro.Builder();
    } else {
      return new com.przemarcz.avro.MealAvro.Builder(other);
    }
  }

  /**
   * RecordBuilder for MealAvro instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<MealAvro>
    implements org.apache.avro.data.RecordBuilder<MealAvro> {

    private java.lang.CharSequence name;
    private java.lang.Object price;
    private java.lang.CharSequence quantity;
    private java.lang.CharSequence image;
    private java.lang.CharSequence ingredients;
    private java.lang.Object timeToDo;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.przemarcz.avro.MealAvro.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.name)) {
        this.name = data().deepCopy(fields()[0].schema(), other.name);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.price)) {
        this.price = data().deepCopy(fields()[1].schema(), other.price);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.quantity)) {
        this.quantity = data().deepCopy(fields()[2].schema(), other.quantity);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
      if (isValidValue(fields()[3], other.image)) {
        this.image = data().deepCopy(fields()[3].schema(), other.image);
        fieldSetFlags()[3] = other.fieldSetFlags()[3];
      }
      if (isValidValue(fields()[4], other.ingredients)) {
        this.ingredients = data().deepCopy(fields()[4].schema(), other.ingredients);
        fieldSetFlags()[4] = other.fieldSetFlags()[4];
      }
      if (isValidValue(fields()[5], other.timeToDo)) {
        this.timeToDo = data().deepCopy(fields()[5].schema(), other.timeToDo);
        fieldSetFlags()[5] = other.fieldSetFlags()[5];
      }
    }

    /**
     * Creates a Builder by copying an existing MealAvro instance
     * @param other The existing instance to copy.
     */
    private Builder(com.przemarcz.avro.MealAvro other) {
      super(SCHEMA$);
      if (isValidValue(fields()[0], other.name)) {
        this.name = data().deepCopy(fields()[0].schema(), other.name);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.price)) {
        this.price = data().deepCopy(fields()[1].schema(), other.price);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.quantity)) {
        this.quantity = data().deepCopy(fields()[2].schema(), other.quantity);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.image)) {
        this.image = data().deepCopy(fields()[3].schema(), other.image);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.ingredients)) {
        this.ingredients = data().deepCopy(fields()[4].schema(), other.ingredients);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.timeToDo)) {
        this.timeToDo = data().deepCopy(fields()[5].schema(), other.timeToDo);
        fieldSetFlags()[5] = true;
      }
    }

    /**
      * Gets the value of the 'name' field.
      * @return The value.
      */
    public java.lang.CharSequence getName() {
      return name;
    }


    /**
      * Sets the value of the 'name' field.
      * @param value The value of 'name'.
      * @return This builder.
      */
    public com.przemarcz.avro.MealAvro.Builder setName(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.name = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'name' field has been set.
      * @return True if the 'name' field has been set, false otherwise.
      */
    public boolean hasName() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'name' field.
      * @return This builder.
      */
    public com.przemarcz.avro.MealAvro.Builder clearName() {
      name = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'price' field.
      * @return The value.
      */
    public java.lang.Object getPrice() {
      return price;
    }


    /**
      * Sets the value of the 'price' field.
      * @param value The value of 'price'.
      * @return This builder.
      */
    public com.przemarcz.avro.MealAvro.Builder setPrice(java.lang.Object value) {
      validate(fields()[1], value);
      this.price = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'price' field has been set.
      * @return True if the 'price' field has been set, false otherwise.
      */
    public boolean hasPrice() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'price' field.
      * @return This builder.
      */
    public com.przemarcz.avro.MealAvro.Builder clearPrice() {
      price = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'quantity' field.
      * @return The value.
      */
    public java.lang.CharSequence getQuantity() {
      return quantity;
    }


    /**
      * Sets the value of the 'quantity' field.
      * @param value The value of 'quantity'.
      * @return This builder.
      */
    public com.przemarcz.avro.MealAvro.Builder setQuantity(java.lang.CharSequence value) {
      validate(fields()[2], value);
      this.quantity = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'quantity' field has been set.
      * @return True if the 'quantity' field has been set, false otherwise.
      */
    public boolean hasQuantity() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'quantity' field.
      * @return This builder.
      */
    public com.przemarcz.avro.MealAvro.Builder clearQuantity() {
      quantity = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'image' field.
      * @return The value.
      */
    public java.lang.CharSequence getImage() {
      return image;
    }


    /**
      * Sets the value of the 'image' field.
      * @param value The value of 'image'.
      * @return This builder.
      */
    public com.przemarcz.avro.MealAvro.Builder setImage(java.lang.CharSequence value) {
      validate(fields()[3], value);
      this.image = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'image' field has been set.
      * @return True if the 'image' field has been set, false otherwise.
      */
    public boolean hasImage() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'image' field.
      * @return This builder.
      */
    public com.przemarcz.avro.MealAvro.Builder clearImage() {
      image = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
      * Gets the value of the 'ingredients' field.
      * @return The value.
      */
    public java.lang.CharSequence getIngredients() {
      return ingredients;
    }


    /**
      * Sets the value of the 'ingredients' field.
      * @param value The value of 'ingredients'.
      * @return This builder.
      */
    public com.przemarcz.avro.MealAvro.Builder setIngredients(java.lang.CharSequence value) {
      validate(fields()[4], value);
      this.ingredients = value;
      fieldSetFlags()[4] = true;
      return this;
    }

    /**
      * Checks whether the 'ingredients' field has been set.
      * @return True if the 'ingredients' field has been set, false otherwise.
      */
    public boolean hasIngredients() {
      return fieldSetFlags()[4];
    }


    /**
      * Clears the value of the 'ingredients' field.
      * @return This builder.
      */
    public com.przemarcz.avro.MealAvro.Builder clearIngredients() {
      ingredients = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    /**
      * Gets the value of the 'timeToDo' field.
      * @return The value.
      */
    public java.lang.Object getTimeToDo() {
      return timeToDo;
    }


    /**
      * Sets the value of the 'timeToDo' field.
      * @param value The value of 'timeToDo'.
      * @return This builder.
      */
    public com.przemarcz.avro.MealAvro.Builder setTimeToDo(java.lang.Object value) {
      validate(fields()[5], value);
      this.timeToDo = value;
      fieldSetFlags()[5] = true;
      return this;
    }

    /**
      * Checks whether the 'timeToDo' field has been set.
      * @return True if the 'timeToDo' field has been set, false otherwise.
      */
    public boolean hasTimeToDo() {
      return fieldSetFlags()[5];
    }


    /**
      * Clears the value of the 'timeToDo' field.
      * @return This builder.
      */
    public com.przemarcz.avro.MealAvro.Builder clearTimeToDo() {
      timeToDo = null;
      fieldSetFlags()[5] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public MealAvro build() {
      try {
        MealAvro record = new MealAvro();
        record.name = fieldSetFlags()[0] ? this.name : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.price = fieldSetFlags()[1] ? this.price :  defaultValue(fields()[1]);
        record.quantity = fieldSetFlags()[2] ? this.quantity : (java.lang.CharSequence) defaultValue(fields()[2]);
        record.image = fieldSetFlags()[3] ? this.image : (java.lang.CharSequence) defaultValue(fields()[3]);
        record.ingredients = fieldSetFlags()[4] ? this.ingredients : (java.lang.CharSequence) defaultValue(fields()[4]);
        record.timeToDo = fieldSetFlags()[5] ? this.timeToDo :  defaultValue(fields()[5]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<MealAvro>
    WRITER$ = (org.apache.avro.io.DatumWriter<MealAvro>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<MealAvro>
    READER$ = (org.apache.avro.io.DatumReader<MealAvro>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}










